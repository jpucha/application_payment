/**
 * 
 */
package ec.com.peigo.repository.payment;

import java.util.List;
import java.util.Optional;

import ec.com.peigo.model.payment.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * <b> Interfaz del repositorio del AccountDto. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface AccountRepository extends JpaRepository<AccountDto, Long> {

	List<AccountDto> findByIdClient(Long id);

	Optional<AccountDto> findByNumber(int number);
}
