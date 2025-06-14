package delivery.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.atomikos.icatch.SysException;

import delivery.auth.services.AuthenticationService;
import delivery.models.dto.ItemDto;
import delivery.models.dto.OrderDto;
import delivery.models.mapper.OrderMapper;
import delivery.models.orders.*;
import delivery.models.orders.auth.Role;
import delivery.services.*;
import lombok.RequiredArgsConstructor;

import java.security.KeyStore.LoadStoreParameter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderProcessorDelegate implements JavaDelegate {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final ItemOrderPoolService itemOrderPoolService;
    private final ItemSellerPoolService itemSellerPoolService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            // Получаем данные из переменных процесса
            Long sellerId = (Long) execution.getVariable("seller_id");
            Long itemId = (Long) execution.getVariable("item_id");
            Integer itemCount = ((Long) execution.getVariable("item_count")).intValue();
            
            var user = (User) execution.getVariable("user");
            
            if (!execution.hasVariable("customer") || !(Boolean) execution.getVariable("customer")) {
                execution.setVariable("orderError", "Только покупатели могут создавать заказы");
                return;
            }
            
            List<ItemDto> items = List.of(new ItemDto(itemId, null, itemCount , null));

            var orderDto = OrderDto.builder()
                .sellerId(sellerId)
                .items(items)
                .build();
            
            // Основная логика из контроллера
            var customer = customerService.getCustomerByUser(user);
            if (customer.isEmpty()) {
                execution.setVariable("orderError", "Покупатель не найден");
                return;
            }
            
            var seller = sellerService.getSellerById(sellerId);
            if (seller.isEmpty()) {
                execution.setVariable("orderError", "Продавец не найден");
                return;
            }
            
            if (orderDto.items() == null || orderDto.items().isEmpty()) {
                execution.setVariable("orderError", "Список товаров пуст");
                return;
            }
            
            if (!itemSellerPoolService.reduceItems(orderDto.items(), seller.get())) {
                execution.setVariable("orderError", "Недостаточно товаров на складе");
                return;
            }
            
            var orderBuilder = Order.builder()
                .customer(customer.get())
                .seller(seller.get())
                .status(OrderStatus.CREATED);
            
            var order = orderService.save(orderBuilder.build());
            
            for (var itemDto : orderDto.items()) {
                if (itemDto.id() == null) {
                    execution.setVariable("orderError", "ID товара не указан");
                    return;
                }
                
                if (itemDto.count() == null) {
                    execution.setVariable("orderError", "Количество товара не указано");
                    return;
                }
                
                var item = itemService.findItemById(itemDto.id());
                if (item.isEmpty()) {
                    execution.setVariable("orderError", "Товар не найден");
                    return;
                }
                
                var itemOrderPool = ItemOrderPool.builder()
                    .order(order)
                    .itemId(item.get().getId())
                    .count(itemDto.count())
                    .build();
                
                itemOrderPoolService.save(itemOrderPool);
            }

            System.out.println("Created order" + order);
            
            // Сохраняем результаты в переменные процесса
            execution.setVariable("orderSuccess", true);
            execution.setVariable("order", order);
            execution.setVariable("seller", seller.get());
            
        } catch (Exception e) {
            execution.setVariable("orderError", "Ошибка при создании заказа: " + e.getMessage());
            throw e;
        }
    }
}