package delivery.repositories.orders;

import delivery.models.orders.Customer;
import delivery.models.orders.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByUser(User user);
}
