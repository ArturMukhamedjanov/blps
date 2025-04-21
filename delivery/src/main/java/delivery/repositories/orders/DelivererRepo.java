package delivery.repositories.orders;

import delivery.models.orders.Distance;
import delivery.models.orders.Deliverer;
import delivery.models.orders.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DelivererRepo extends JpaRepository<Deliverer, Long> {
    Optional<Deliverer> findDelivererByUser(User user);
    List<Deliverer> getDeliverersByDistance(Distance distance);
}
