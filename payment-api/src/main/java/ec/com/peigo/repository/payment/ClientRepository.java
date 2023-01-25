/**
 * 
 */
package ec.com.peigo.repository.payment;

import java.util.Optional;

import ec.com.peigo.model.payment.ClientDto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * <b> Interfaz del repositorio del cliente. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface ClientRepository extends JpaRepository<ClientDto, Long> {

	Optional<ClientDto> findByStateAndIdClient(String estado, Long id);

	Optional<ClientDto> findByIdClient(Long id);

	Optional<ClientDto> findByIdentification(String id);

}
