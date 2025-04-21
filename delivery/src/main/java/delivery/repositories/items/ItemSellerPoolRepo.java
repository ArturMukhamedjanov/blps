package delivery.repositories.items;

import delivery.models.items.Item;
import delivery.models.items.ItemSellerPool;
import delivery.models.orders.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemSellerPoolRepo extends JpaRepository<ItemSellerPool, Long> {
    List<ItemSellerPool> getItemSellerPoolsByItem(Item item);
    List<ItemSellerPool> getItemSellerPoolsBySeller(Seller seller);
    Optional<ItemSellerPool> findByItemAndSeller(Item item, Seller seller);
}
