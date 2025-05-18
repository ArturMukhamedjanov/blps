package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.dto.*;
import delivery.models.items.ItemSellerPool;
import delivery.models.mapper.*;
import delivery.models.orders.*;
import delivery.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

    private final AuthenticationService authenticationService;
    private final SellerService sellerService;
    private final ItemService itemService;
    private final ItemSellerPoolService itemSellerPoolService;
    private final ItemOrderPoolService itemOrderPoolService;
    private final OrderService orderService;
    private final DelivererService delivererService;
    private final OrderMapper orderMapper;

    private final SellerMapper sellerMapper;
    private final ItemMapper itemMapper;

    // Получение информации о клинике для текущего пользователя
    @GetMapping()
    public ResponseEntity<SellerDto> getSeller() {
        var currentUser = authenticationService.getCurrentUser();
        var seller = sellerService.getSellerByUser(currentUser);
        if (seller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var sellerDto = sellerMapper.mapToDto(seller.get());
        sellerDto.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(sellerDto);
    }

    @PostMapping
    public ResponseEntity<SellerDto> updateSellerInfo(@Valid @RequestBody SellerDto sellerDto) {
        var currentUser = authenticationService.getCurrentUser();
        var currentSeller = sellerService.getSellerByUser(currentUser);
        if (currentSeller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedSeller = mergeSellers(currentSeller.get(), sellerDto);
        updatedSeller = sellerService.updateSeller(updatedSeller);
        var res = sellerMapper.mapToDto(updatedSeller);
        res.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/item/{id}")
    public ResponseEntity<ItemDto> addItem(@PathVariable Long id, @RequestBody ItemDto itemDto){
        var item = itemService.findItemById(id);
        if(item.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        if(seller.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(!validateItem(itemDto)){
            return ResponseEntity.badRequest().build();
        }
        if(itemSellerPoolService.findByItemAndSeller(item.get(), seller.get()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        var itemPool = ItemSellerPool.builder()
                .item(item.get())
                .sellerId(seller.get().getId())
                .count(itemDto.count())
                .price(itemDto.price())
                .build();
        itemPool = itemSellerPoolService.save(itemPool);
        var result = itemMapper.mapToDto(itemPool);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto){
        var item = itemService.findItemById(id);
        if(item.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        if(seller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(!validateItem(itemDto)){
            return ResponseEntity.badRequest().build();
        }
        var itemPool = itemSellerPoolService.findByItemAndSeller(item.get(), seller.get());
        if(itemPool.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        itemPool.get().setCount(itemDto.count());
        itemPool.get().setPrice(itemDto.price());
        var newItemPool = itemSellerPoolService.save(itemPool.get());
        var result = itemMapper.mapToDto(newItemPool);
        return ResponseEntity.ok(result);
    }

    @PostMapping("order/reject/{order_id}")
    public ResponseEntity<Void> rejectOrder(@PathVariable("order_id") Long orderId){
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if(orderOpt.isEmpty() || orderOpt.get().getStatus() != OrderStatus.CREATED || seller.isEmpty() || orderOpt.get().getSeller().getId() != seller.get().getId()){
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(OrderStatus.REJECTED_BY_SELLER);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/accept/{order_id}")
    public ResponseEntity<Void> acceptOrder(@PathVariable("order_id") Long orderId){
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        var orderOpt = orderService.findById(orderId);
        if(orderOpt.isEmpty() || orderOpt.get().getStatus() != OrderStatus.CREATED || seller.isEmpty() || orderOpt.get().getSeller().getId() != seller.get().getId()){
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(OrderStatus.ACCEPTED_BY_SELLER);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/update/{order_id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("order_id") Long orderId, @RequestBody OrderDto orderDto) {
        var orderOpt = orderService.findById(orderId);
        var user = authenticationService.getCurrentUser();
        var seller = sellerService.getSellerByUser(user);
        if(seller.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if (orderOpt.isEmpty() || (orderOpt.get().getStatus() != OrderStatus.ACCEPTED_BY_SELLER && orderOpt.get().getStatus() != OrderStatus.CREATED)
        ) {
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        if(orderDto.items() == null || orderDto.items().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        for(var itemDto : orderDto.items()){
            if(itemDto.id() == null){
                return ResponseEntity.badRequest().build();
            }
            if(itemDto.count() == null){
                return ResponseEntity.badRequest().build();
            }
            var item = itemService.findItemById(itemDto.id());
            if(item.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
        }
        if(!itemSellerPoolService.update(orderDto.items(), seller.get(), order)){
            return ResponseEntity.badRequest().build();
        }
        if(orderDto.sellerDescription() != null){
            order.setSellerDescription(orderDto.sellerDescription());
        }
        order.setStatus(OrderStatus.UPDATED_BY_SELLER);
        order = orderService.save(order);
        var newOrderDto = orderMapper.mapToDto(order).toBuilder().items(orderDto.items()).build();
        return ResponseEntity.ok(newOrderDto);
    }

    @PostMapping("/order/pack/{order_id}")
    public ResponseEntity<Void> packOrder(@PathVariable("order_id") Long orderId){
        var orderOpt = orderService.findById(orderId);
        if(orderOpt.isEmpty() || (orderOpt.get().getStatus() != OrderStatus.UPDATED_BY_SELLER && orderOpt.get().getStatus() != OrderStatus.ACCEPTED_BY_SELLER) ){
            System.out.println("Rejected not right status:" + orderOpt);
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(OrderStatus.PACKED);
        orderService.save(order);
        orderService.sendOrderAssembledEvent(orderMapper.mapToDto(order));
        return ResponseEntity.ok().build();
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

    public Seller mergeSellers(Seller oldSeller, SellerDto newSeller) {
        if (newSeller.name() != null) {
            oldSeller.setName(newSeller.name());
        }
        return oldSeller;
    }

    private Boolean validateItem(ItemDto itemDto){
        return itemDto.count() != null && itemDto.price() != null && itemDto.count() > 0 && itemDto.price() > 0;
    }

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

}
