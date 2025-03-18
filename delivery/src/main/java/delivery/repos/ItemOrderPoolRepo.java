package delivery.repos;

import delivery.models.Item;
import delivery.models.ItemOrderPool;
import delivery.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemOrderPoolRepo extends JpaRepository<ItemOrderPool, Long> {

    List<ItemOrderPool> getItemOrderPoolsByOrder(Order order);
    List<ItemOrderPool> getItemOrderPoolsByItem(Item item);
    Optional<ItemOrderPool> findByOrderAndItem(Order order, Item item);
    void deleteByOrder(Order order);
}
