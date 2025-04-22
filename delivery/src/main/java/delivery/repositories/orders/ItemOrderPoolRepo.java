package delivery.repositories.orders;

import delivery.models.items.Item;
import delivery.models.orders.ItemOrderPool;
import delivery.models.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemOrderPoolRepo extends JpaRepository<ItemOrderPool, Long> {

    List<ItemOrderPool> getItemOrderPoolsByOrder(Order order);
    List<ItemOrderPool> getItemOrderPoolsByItemId(long itemId);
    Optional<ItemOrderPool> findByOrderAndItemId(Order order, long itemId);
    void deleteByOrder(Order order);
}
