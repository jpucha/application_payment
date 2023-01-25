package ec.com.peigo.repository.payment;

import ec.com.peigo.model.AuthorityDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityDto, Integer> {

}
