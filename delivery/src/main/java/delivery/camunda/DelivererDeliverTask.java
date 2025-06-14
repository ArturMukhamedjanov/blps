package delivery.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import delivery.auth.AuthenticationRequest;
import delivery.auth.services.AuthenticationService;
import delivery.models.mapper.OrderMapper;
import delivery.models.orders.Order;
import delivery.models.orders.OrderStatus;
import delivery.models.orders.auth.Role;
import delivery.services.OrderService;
import delivery.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DelivererDeliverTask implements JavaDelegate {
    
    private final OrderService orderService;
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var order = (Order) delegateExecution.getVariable("order");
        order.setStatus(OrderStatus.DELIVERED);
        orderService.save(order);
        delegateExecution.setVariable("order", order);
    }

}