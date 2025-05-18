package delivery.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import delivery.models.orders.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderBanningService {

    private final OrderService orderService;

    /**
     * Метод выполняется каждые 30 минут
     * (fixedRate = 30 * 60 * 1000 миллисекунд)
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void collectDeliveryStatistics() {
        try {
            System.out.println("Запуск удаления заказов...");
            var orders = orderService.getAllOrders();
            for(var order : orders){
                if(order.getStatus() == OrderStatus.PACKED){
                    orderService.delete(order);
                }
            }
            System.out.println("Конец удаления заказов");
        } catch (Exception e) {
            System.out.println("Запуск удаления заказов" +  e.getMessage());
        }
    }
}
