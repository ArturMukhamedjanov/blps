package delivery.repositories.orders;

import delivery.models.orders.Customer;
import delivery.models.orders.Deliverer;
import delivery.models.orders.Order;
import delivery.models.orders.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> getOrdersByCustomer(Customer customer);
    List<Order> getOrdersBySeller(Seller seller);
    List<Order> getOrdersByDeliverer(Deliverer deliverer);
    List<Order> getOrdersByCustomerAndSeller(Customer customer, Seller seller);
    List<Order> getOrdersByCustomerAndDeliverer(Customer customer, Deliverer deliverer);
    List<Order> getOrdersBySellerAndDeliverer(Seller seller, Deliverer deliverer);
    List<Order> getOrdersByCustomerAndSellerAndDeliverer(Customer customer, Seller seller, Deliverer deliverer);
}
