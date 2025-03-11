package delivery.repos;

import delivery.models.Item;
import delivery.models.ItemSellerPool;
import delivery.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemSellerPoolRepo extends JpaRepository<ItemSellerPool, Long> {
    List<ItemSellerPool> getItemSellerPoolsByItem(Item item);
    List<ItemSellerPool> getItemSellerPoolsBySeller(Seller seller);
    Optional<ItemSellerPool> findByItemAndSeller(Item item, Seller seller);
}
