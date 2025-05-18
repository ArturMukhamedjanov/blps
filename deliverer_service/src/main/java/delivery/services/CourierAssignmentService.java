package delivery.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import delivery.models.dto.OrderDto;
import delivery.models.orders.Deliverer;
import delivery.models.orders.Distance;
import delivery.models.orders.Order;
import delivery.models.orders.OrderStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourierAssignmentService {
    private static final String ORDER_ASSEMBLED_QUEUE = "order.assembled.queue";
    private static final String COURIER_ASSIGNED_QUEUE = "courier.assigned.queue";
    private static final long RETRY_DELAY_MS = 5000;

    private final DelivererService delivererService;
    private final OrderService orderService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = ORDER_ASSEMBLED_QUEUE)
    public void handleOrderAssembled(OrderDto message) {
        System.out.println("Recieved message = " + message);
        var orderOpt = orderService.findById(message.id());
        if(orderOpt.isEmpty()){
            System.out.println("Not found order id for message = " + message);
            return;
        }
        var order = orderOpt.get();
        if(order.getStatus() == OrderStatus.ON_MODERATION){
            System.out.println("Order banned = " + message);
            return;
        }
        var deliverer = setDeliverer(order);
        if(deliverer.isEmpty()){
            retryAssignment(message);
            return;
        }
        order.setDeliverer(deliverer.get());
        order.setStatus(OrderStatus.SET_TO_DELIVERER);
        orderService.save(order);
        System.out.println(message);
    }

    public void retryAssignment(OrderDto order) {
        try {
            // Устанавливаем задержку через заголовок
            System.out.println("Wait to resend the message = " + order);
            Thread.sleep(RETRY_DELAY_MS);
            jmsTemplate.convertAndSend(ORDER_ASSEMBLED_QUEUE, order);
            System.out.println("Retrying order assignment in 5 minutes: " + order.id());
        } catch (Exception e) {
            System.err.println("Failed to reschedule order assignment: " + e.getMessage());
        }
    }


    private Optional<Deliverer> setDeliverer(Order order) {
        var startX = order.getSeller().getX();
        var startY = order.getSeller().getY();
        var endX = order.getCustomer().getX();
        var endY = order.getCustomer().getY();
        var distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        System.out.println("Counted distance: " + distance);
        var deliverers = new ArrayList<Deliverer>();
        if(distance < 100){
            deliverers.addAll(delivererService.getDeliverersByDistance(Distance.SHORT));
            System.out.println(deliverers.size());
        }
        if(distance < 200){
            deliverers.addAll(delivererService.getDeliverersByDistance(Distance.MEDIUM));
            System.out.println(deliverers.size());
        }
        deliverers.addAll(delivererService.getDeliverersByDistance(Distance.LONG));
        System.out.println(deliverers.size());
        for(var deliverer : deliverers){
            if(deliverer.isFree()){
                deliverer.setFree(false);
                delivererService.saveDeliverer(deliverer);
                return Optional.of(deliverer);
            }
        }
        return Optional.empty();
    }

}