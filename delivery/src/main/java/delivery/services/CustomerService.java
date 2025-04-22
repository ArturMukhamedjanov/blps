package delivery.services;

import delivery.models.orders.Customer;
import delivery.models.orders.User;
import delivery.repositories.orders.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepo.findById(id);
    }

    public Optional<Customer> getCustomerByUser(User currentUser) {
        return customerRepo.findCustomerByUser(currentUser);
    }

    public Customer updateCustomer(Customer updatedCustomer) {
        return customerRepo.save(updatedCustomer);
    }
}
