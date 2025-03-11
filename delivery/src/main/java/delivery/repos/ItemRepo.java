package delivery.repos;

import delivery.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepo extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
}
