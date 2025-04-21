package delivery.services;

import delivery.models.orders.Customer;
import delivery.models.orders.Deliverer;
import delivery.models.orders.Order;
import delivery.models.orders.Seller;
import delivery.repositories.orders.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepo.getOrdersByCustomer(customer);
    }

    public List<Order> getOrdersBySeller(Seller seller) {
        return orderRepo.getOrdersBySeller(seller);
    }

    public List<Order> getOrdersByDeliverer(Deliverer deliverer) {
        return orderRepo.getOrdersByDeliverer(deliverer);
    }

    public List<Order> getOrdersByCustomerAndSeller(Customer customer, Seller seller) {
        return orderRepo.getOrdersByCustomerAndSeller(customer, seller);
    }

    public List<Order> getOrdersByCustomerAndDeliverer(Customer customer, Deliverer deliverer) {
        return orderRepo.getOrdersByCustomerAndDeliverer(customer, deliverer);
    }

    public List<Order> getOrdersBySellerAndDeliverer(Seller seller, Deliverer deliverer) {
        return orderRepo.getOrdersBySellerAndDeliverer(seller, deliverer);
    }

    public List<Order> getOrdersByCustomerSellerAndDeliverer(Customer customer, Seller seller, Deliverer deliverer) {
        return orderRepo.getOrdersByCustomerAndSellerAndDeliverer(customer, seller, deliverer);
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public void delete(Order order) {
        orderRepo.delete(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepo.findById(id);
    }
}