package delivery.repositories.items;

import delivery.models.items.Item;
import delivery.models.items.ItemSellerPool;
import delivery.models.orders.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemSellerPoolRepo extends JpaRepository<ItemSellerPool, Long> {
    List<ItemSellerPool> getItemSellerPoolsByItem(Item item);
    List<ItemSellerPool> getItemSellerPoolsBySellerId(long sellerId);
    Optional<ItemSellerPool> findByItemAndSellerId(Item item, long sellerId);
}
