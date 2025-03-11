package delivery.repos;

import delivery.models.Seller;
import delivery.models.Deliverer;
import delivery.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DelivererRepo extends JpaRepository<Deliverer, Long> {
    Optional<Deliverer> findDelivererByUser(User user);
    List<Deliverer> getDeliverersByClinic(Seller seller);
}
