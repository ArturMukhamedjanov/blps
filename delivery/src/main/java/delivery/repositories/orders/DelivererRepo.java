package delivery.repositories.orders;

import delivery.models.orders.Distance;
import delivery.models.orders.User;
import delivery.models.orders.Deliverer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DelivererRepo extends JpaRepository<Deliverer, Long> {
    Optional<Deliverer> findDelivererByUser(User user);
    List<Deliverer> getDeliverersByDistance(Distance distance);
}
