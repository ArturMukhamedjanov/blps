package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.Deliverer;
import delivery.models.Distance;
import delivery.models.Order;
import delivery.models.OrderStatus;
import delivery.models.dto.*;
import delivery.models.mapper.*;
import delivery.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/deliverer")
public class DelivererController {

    private final AuthenticationService authenticationService;
    private final DelivererService delivererService;
    private final OrderService orderService;
    private final DelivererMapper delivererMapper;

    // Получение информации о докторе для текущего пользователя
    @GetMapping()
    public ResponseEntity<DelivererDto> getDeliverer() {
        var currentUser = authenticationService.getCurrentUser();
        var deliverer = delivererService.getDelivererByUser(currentUser);
        if (deliverer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var delivererDto = delivererMapper.mapToDto(deliverer.get());
        delivererDto.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(delivererDto);
    }

    // Обновление информации о докторе
    @PostMapping
    public ResponseEntity<DelivererDto> updateDelivererInfo(@Valid @RequestBody DelivererDto delivererDto) {
        var currentUser = authenticationService.getCurrentUser();
        var currentDeliverer = delivererService.getDelivererByUser(currentUser);
        if (currentDeliverer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedDeliverer = mergeDeliverers(currentDeliverer.get(), delivererDto);
        updatedDeliverer = delivererService.updateDeliverer(updatedDeliverer);
        var res = delivererMapper.mapToDto(updatedDeliverer);
        res.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/order/reject/{order_id}")
    public ResponseEntity<Void> rejectOrder(@PathVariable("order_id") Long orderId){
        var delivererOpt = delivererService.getDelivererByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if(
                orderOpt.isEmpty() ||
                orderOpt.get().getStatus() != OrderStatus.SET_TO_DELIVERER ||
                delivererOpt.isEmpty() ||
                orderOpt.get().getDeliverer() != delivererOpt.get()
        ){
            return ResponseEntity.notFound().build();
        }
        var deliverer = delivererOpt.get();
        var order = orderOpt.get();
        order.setStatus(OrderStatus.PACKED);
        orderService.save(order);
        var newDelivererOpt = getNewDeliverer(order);
        deliverer.setFree(true);
        delivererService.saveDeliverer(deliverer);
        if(newDelivererOpt.isPresent()){
            var newDeliverer = newDelivererOpt.get();
            newDeliverer.setFree(false);
            delivererService.saveDeliverer(newDeliverer);
            order.setDeliverer(newDeliverer);
            order.setStatus(OrderStatus.SET_TO_DELIVERER);
            orderService.save(order);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/accept/{order_id}")
    public ResponseEntity<Void> acceptOrder(@PathVariable("order_id") Long orderId){
        var deliverer = delivererService.getDelivererByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if(
                orderOpt.isEmpty() ||
                orderOpt.get().getStatus() != OrderStatus.SET_TO_DELIVERER ||
                deliverer.isEmpty() ||
                orderOpt.get().getDeliverer() != deliverer.get()
        ){
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(OrderStatus.ACCEPTED_BY_DELIVERER);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/receive/{order_id}")
    public ResponseEntity<Void> receiveOrder(@PathVariable("order_id") Long orderId){
        var deliverer = delivererService.getDelivererByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if(
                orderOpt.isEmpty() ||
                orderOpt.get().getStatus() != OrderStatus.ACCEPTED_BY_DELIVERER ||
                deliverer.isEmpty() ||
                orderOpt.get().getDeliverer() != deliverer.get()
        ){
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(OrderStatus.RECEIVED_BY_DELIVERER);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/deliver/{order_id}")
    public ResponseEntity<Void> deliverOrder(@PathVariable("order_id") Long orderId) {
        var delivererOpt = delivererService.getDelivererByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if (
                orderOpt.isEmpty() ||
                        orderOpt.get().getStatus() != OrderStatus.RECEIVED_BY_DELIVERER ||
                        delivererOpt.isEmpty() ||
                        orderOpt.get().getDeliverer() != delivererOpt.get()
        ) {
            return ResponseEntity.notFound().build();
        }
        var deliverer = delivererOpt.get();
        deliverer.setFree(true);
        delivererService.saveDeliverer(deliverer);
        var order = orderOpt.get();
        order.setStatus(OrderStatus.DELIVERED);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    private Optional<Deliverer> getNewDeliverer(Order order) {
        var startX = order.getSeller().getX();
        var startY = order.getSeller().getY();
        var endX = order.getCustomer().getX();
        var endY = order.getCustomer().getY();
        var distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        var deliverers = new ArrayList<Deliverer>();
        if(distance < 100){
            deliverers.addAll(delivererService.getDeliverersByDistance(Distance.SHORT));
        }
        if(distance < 200){
            deliverers.addAll(delivererService.getDeliverersByDistance(Distance.MEDIUM));
        }
        deliverers.addAll(delivererService.getDeliverersByDistance(Distance.LONG));
        for(var deliverer : deliverers){
            if(deliverer.isFree()){
                deliverer.setFree(false);
                delivererService.saveDeliverer(deliverer);
                return Optional.of(deliverer);
            }
        }
        return Optional.empty();
    }

    public Deliverer mergeDeliverers(Deliverer oldDeliverer, DelivererDto newDeliverer) {
        return oldDeliverer;
    }
}
