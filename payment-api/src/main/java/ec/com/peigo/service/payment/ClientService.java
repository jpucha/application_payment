/**
 * 
 */
package ec.com.peigo.service.payment;

import java.util.List;
import java.util.Optional;

import ec.com.peigo.model.payment.ClientDto;

/**
 * 
 * <b> Interfaz del servicio para el cliente. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface ClientService {
	ClientDto create(ClientDto clientDto);

	List<ClientDto> read();

	ClientDto update(ClientDto clientDto);

	void delete(Long id);

	Optional<ClientDto> getByStateIdClient(String estado, Long id);

	Optional<ClientDto> getById(Long id);

	Optional<ClientDto> getByIdClient(Long id);

	Optional<ClientDto> getByIdentification(String identification);

}
