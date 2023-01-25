/**
 * 
 */
package ec.com.peigo.service.payment.impl;

import java.util.List;
import java.util.Optional;

import ec.com.peigo.model.payment.ClientDto;
import ec.com.peigo.repository.payment.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.peigo.service.payment.ClientService;

/**
 * 
 * <b> Servicio para el cliente. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public ClientDto create(ClientDto clientDto) {
		return clientRepository.save(clientDto);
	}

	@Override
	public List<ClientDto> read() {
		return clientRepository.findAll();
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		return clientRepository.save(clientDto);
	}

	@Override
	public void delete(Long id) {
		clientRepository.deleteById(id);

	}

	@Override
	public Optional<ClientDto> getByStateIdClient(String estado, Long id) {
		return clientRepository.findByStateAndIdClient(estado, id);
	}

	@Override
	public Optional<ClientDto> getById(Long id) {
		return clientRepository.findById(id);
	}

	@Override
	public Optional<ClientDto> getByIdClient(Long id) {
		return clientRepository.findByIdClient(id);
	}

	@Override
	public Optional<ClientDto> getByIdentification(String identificacion) {
		return clientRepository.findByIdentification(identificacion);
	}

}
