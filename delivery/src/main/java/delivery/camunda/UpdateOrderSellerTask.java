package delivery.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import delivery.auth.services.AuthenticationService;
import delivery.models.dto.ItemDto;
import delivery.models.dto.OrderDto;
import delivery.models.items.ItemSellerPool;
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
public class UpdateOrderSellerTask implements JavaDelegate{

    private final ItemService itemService;
    private final OrderService orderService;
    private final ItemSellerPoolService itemSellerPoolService;
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try{
            delegateExecution.setVariable("updated", false);
            var order = (Order) delegateExecution.getVariable("order");
            var seller = order.getSeller();
            var customer = order.getCustomer();
            Long itemId = (Long) delegateExecution.getVariable("item_id");
            Integer itemCount = ((Long) delegateExecution.getVariable("item_count")).intValue();
            List<ItemDto> itemsDto = List.of(new ItemDto(itemId, null, itemCount , null));
            for(var itemDto : itemsDto){
                if(itemDto.id() == null){
                    return;
                }
                if(itemDto.count() == null){
                    return;
                }
                var item = itemService.findItemById(itemDto.id());
                if(item.isEmpty()){
                    return;
                }
            }
            if(!itemSellerPoolService.update(itemsDto, seller, order)){
                return;
            }    
            order.setStatus(OrderStatus.UPDATED_BY_SELLER);
            order = orderService.save(order);
            delegateExecution.setVariable("order", order);
            delegateExecution.setVariable("updated", true);
        } catch (Exception e){
            return;
        }
    }

}
