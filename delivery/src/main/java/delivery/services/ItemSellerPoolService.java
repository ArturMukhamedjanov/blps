package delivery.services;

import delivery.models.Item;
import delivery.models.ItemSellerPool;
import delivery.models.Seller;
import delivery.repos.ItemSellerPoolRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemSellerPoolService {

    private final ItemSellerPoolRepo itemSellerPoolRepo;

    public List<ItemSellerPool> getItemPoolsByItem(Item item) {
        return itemSellerPoolRepo.getItemSellerPoolsByItem(item);
    }

    public List<ItemSellerPool> getItemPoolsBySeller(Seller seller) {
        return itemSellerPoolRepo.getItemSellerPoolsBySeller(seller);
    }

    public Optional<ItemSellerPool> findByItemAndSeller(Item item, Seller seller) {
        return itemSellerPoolRepo.findByItemAndSeller(item, seller);
    }

    public ItemSellerPool save(ItemSellerPool itemSellerPool) {
        return itemSellerPoolRepo.save(itemSellerPool);
    }

    public void delete(ItemSellerPool itemSellerPool) {
        itemSellerPoolRepo.delete(itemSellerPool);
    }
}