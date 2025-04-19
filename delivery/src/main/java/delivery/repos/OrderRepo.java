package delivery.repos;

import delivery.models.Customer;
import delivery.models.Deliverer;
import delivery.models.Order;
import delivery.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> getOrdersByCustomer(Customer customer);
    List<Order> getOrdersBySeller(Seller seller);
    List<Order> getOrdersByDeliverer(Deliverer deliverer);
    List<Order> getOrdersByCustomerAndSeller(Customer customer, Seller seller);
    List<Order> getOrdersByCustomerAndDeliverer(Customer customer, Deliverer deliverer);
    List<Order> getOrdersBySellerAndDeliverer(Seller seller, Deliverer deliverer);
    List<Order> getOrdersByCustomerAndSellerAndDeliverer(Customer customer, Seller seller, Deliverer deliverer);
}
