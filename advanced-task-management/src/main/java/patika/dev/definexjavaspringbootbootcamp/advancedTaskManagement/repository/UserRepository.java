package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String mail);
}
