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
    List<Order> getOrdersDeliverer(Deliverer deliverer);
    List<Order> getOrdersCustomerAndSeller(Customer customer, Seller seller);
    List<Order> getOrdersCustomerAndDeliverer(Customer customer, Deliverer deliverer);
    List<Order> getOrdersSellerAndDeliverer(Seller seller, Deliverer deliverer);
    List<Order> getOrdersCustomerAndSellerAndDeliverer(Customer customer, Seller seller, Deliverer deliverer);
}
