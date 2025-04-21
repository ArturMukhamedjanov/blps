package delivery.services;

import delivery.models.items.Item;
import delivery.repositories.items.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    public Optional<Item> findItemById(Long id) {
        return itemRepo.findById(id);
    }

    public Optional<Item> findItemByName(String name) {
        return itemRepo.findByName(name);
    }

    public Item saveItem(Item item) {
        return itemRepo.save(item);
    }

    public Item updateItem(Item item) {
        return itemRepo.save(item);
    }

    public void deleteItem(Long id) {
        itemRepo.deleteById(id);
    }
}
