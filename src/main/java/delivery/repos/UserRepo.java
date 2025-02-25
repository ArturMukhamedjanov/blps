package delivery.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import delivery.models.auth.User;

public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);
}
