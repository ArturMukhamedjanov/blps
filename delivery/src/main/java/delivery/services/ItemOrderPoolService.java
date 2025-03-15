package delivery.services;

import delivery.models.Item;
import delivery.models.ItemOrderPool;
import delivery.models.Order;
import delivery.repos.ItemOrderPoolRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemOrderPoolService {

    private final ItemOrderPoolRepo itemOrderPoolRepo;

    public List<ItemOrderPool> getItemPoolsByOrder(Order order) {
        return itemOrderPoolRepo.getItemOrderPoolsByOrder(order);
    }

    public List<ItemOrderPool> getItemPoolsByItem(Item item) {
        return itemOrderPoolRepo.getItemOrderPoolsByItem(item);
    }

    public Optional<ItemOrderPool> findByOrderAndItem(Order order, Item item) {
        return itemOrderPoolRepo.findByOrderAndItem(order, item);
    }

    public ItemOrderPool save(ItemOrderPool itemOrderPool) {
        return itemOrderPoolRepo.save(itemOrderPool);
    }

    public void delete(ItemOrderPool itemOrderPool) {
        itemOrderPoolRepo.delete(itemOrderPool);
    }

}