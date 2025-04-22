package delivery.repositories.orders;

import delivery.models.orders.Seller;
import delivery.models.orders.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {
    Optional<Seller> findSellerByUser(User user);

}
