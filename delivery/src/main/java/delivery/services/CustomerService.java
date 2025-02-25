package delivery.services;

import delivery.models.Customer;
import delivery.models.auth.User;
import delivery.repos.CustomerRepo;
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
