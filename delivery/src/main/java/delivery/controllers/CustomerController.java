package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.*;
import delivery.models.auth.Role;
import delivery.models.dto.*;
import delivery.models.mapper.*;
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

    public Customer mergeCustomers(Customer oldCustomer, CustomerDto newCustomer){
        return oldCustomer;
    }

}
