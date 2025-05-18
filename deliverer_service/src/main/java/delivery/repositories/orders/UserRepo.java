package delivery.repositories.orders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import delivery.models.orders.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);
}
