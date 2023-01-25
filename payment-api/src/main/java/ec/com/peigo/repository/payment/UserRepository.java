package ec.com.peigo.repository.payment;

import ec.com.peigo.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Integer> {
    UserDto findByUsername(String username);
}