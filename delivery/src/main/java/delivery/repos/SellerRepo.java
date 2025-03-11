package delivery.repos;

import delivery.models.Seller;
import delivery.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller, Long> {
    Optional<Seller> findSellerByUser(User user);

}
