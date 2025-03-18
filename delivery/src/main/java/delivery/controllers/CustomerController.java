package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.*;
import delivery.models.auth.Role;
import delivery.models.dto.*;
import delivery.models.mapper.*;
import delivery.repos.ItemSellerPoolRepo;
import delivery.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final ItemService itemService;
    private final ItemSellerPoolService itemSellerPoolService;
    private final ItemOrderPoolService itemOrderPoolService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;


    @GetMapping()
    public ResponseEntity<CustomerDto> getCustomer() {
        var currentUser = authenticationService.getCurrentUser();
        var customer = customerService.getCustomerByUser(currentUser);
        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var customerDto = customerMapper.mapToDto(customer.get());
        customerDto.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> updateCustomerInfo(@Valid @RequestBody CustomerDto customerDto) {
        var currentUser = authenticationService.getCurrentUser();
        var currentCustomer = customerService.getCustomerByUser(currentUser);
        if (currentCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedCustomer = mergeCustomers(currentCustomer.get(), customerDto);
        updatedCustomer = customerService.updateCustomer(updatedCustomer);
        var res = customerMapper.mapToDto(updatedCustomer);
        res.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(res);
    }

    @PostMapping("order/{sellerId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long sellerId, @RequestBody OrderDto orderDtos) {
        var user = authenticationService.getCurrentUser();
        var customer = customerService.getCustomerByUser(user);
        if(customer.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var seller = sellerService.getSellerById(sellerId);
        if(seller.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(orderDtos.items() == null || orderDtos.items().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if(!itemSellerPoolService.reduceItems(orderDtos.items(), seller.get())){
            return ResponseEntity.badRequest().build();
        }
        var orderBuilder = Order.builder()
                .customer(customer.get())
                .seller(seller.get())
                .status(OrderStatus.CREATED);
        if(orderDtos.description() != null){
            orderBuilder.description(orderDtos.description());
        }
        var order = orderService.save(orderBuilder.build());
        for(var itemDto : orderDtos.items()){
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
            var itemOrderPool = ItemOrderPool.builder()
                    .order(order)
                    .item(item.get())
                    .count(itemDto.count())
                    .build();
            itemOrderPoolService.save(itemOrderPool);
        }
        var orderDto = orderMapper.mapToDto(order).toBuilder().items(orderDtos.items()).build();
        return ResponseEntity.ok(orderDto);

    }

    public Customer mergeCustomers(Customer oldCustomer, CustomerDto newCustomer){
        return oldCustomer;
    }

}
