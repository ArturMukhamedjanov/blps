package delivery.services;

import delivery.models.Customer;
import delivery.models.Deliverer;
import delivery.models.Order;
import delivery.models.Seller;
import delivery.repos.OrderRepo;
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
        return orderRepo.getOrdersDeliverer(deliverer);
    }

    public List<Order> getOrdersByCustomerAndSeller(Customer customer, Seller seller) {
        return orderRepo.getOrdersCustomerAndSeller(customer, seller);
    }

    public List<Order> getOrdersByCustomerAndDeliverer(Customer customer, Deliverer deliverer) {
        return orderRepo.getOrdersCustomerAndDeliverer(customer, deliverer);
    }

    public List<Order> getOrdersBySellerAndDeliverer(Seller seller, Deliverer deliverer) {
        return orderRepo.getOrdersSellerAndDeliverer(seller, deliverer);
    }

    public List<Order> getOrdersByCustomerSellerAndDeliverer(Customer customer, Seller seller, Deliverer deliverer) {
        return orderRepo.getOrdersCustomerAndSellerAndDeliverer(customer, seller, deliverer);
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