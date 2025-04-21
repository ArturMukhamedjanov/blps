package delivery.repositories.orders;

import delivery.models.orders.Seller;
import delivery.models.orders.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller, Long> {
    Optional<Seller> findSellerByUser(User user);

}
