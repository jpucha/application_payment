/**
 * 
 */
package ec.com.peigo.controller.payment;

import java.util.Optional;

import ec.com.peigo.model.payment.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.com.peigo.controller.payment.vo.ClientRequest;
import ec.com.peigo.service.payment.ClientService;

/**
 * 
 * <b> Clase controlador de los clientes. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@RestController
@RequestMapping("/api/clientes")
public class ClientController {
	private static final Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService service;

	/**
	 * 
	 * <b> Metodo que crea un cliente. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param clienteEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PostMapping
	public ResponseEntity<?> create(@Validated @RequestBody ClientRequest clienteEntradaDto) {
		try {

			Optional<ClientDto> clienteEncontrado = service
					.getByIdentification(clienteEntradaDto.getIdentificacion());
			if (clienteEncontrado.isPresent()) {
				return new ResponseEntity<>(
						"ClientDto ya se encuentra registrado es estado: " + clienteEncontrado.get().getState(),
						HttpStatus.BAD_REQUEST);
			} else {
				ClientDto clientDto = new ClientDto();
				clientDto.setName(clienteEntradaDto.getNombre());
				clientDto.setGender(clienteEntradaDto.getGenero());
				clientDto.setAge(clienteEntradaDto.getEdad());
				clientDto.setIdentification(clienteEntradaDto.getIdentificacion());
				clientDto.setAddress(clienteEntradaDto.getDireccion());
				clientDto.setPhoneNumber(clienteEntradaDto.getTelefono());
				clientDto.setPassword(clienteEntradaDto.getContrasena());
				clientDto.setState(Boolean.TRUE.toString());
				ClientDto clientDtoGuardado = service.create(clientDto);
				return new ResponseEntity<ClientDto>(clientDtoGuardado, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * <b> Metodo para obtiene un cliente por su identidicacion. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param clienteEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@GetMapping
	public ResponseEntity<?> obtenerCliente(@Validated @RequestBody ClientRequest clienteEntradaDto) {
		try {
			Optional<ClientDto> clienteEncontrado = service
					.getByIdentification(clienteEntradaDto.getIdentificacion());
			if (clienteEncontrado.isPresent()) {
				return new ResponseEntity<ClientDto>(clienteEncontrado.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("ClientDto no existe.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * <b> Metodo que actualiza un cliente. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param clienteEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PutMapping
	public ResponseEntity<?> update(@Validated @RequestBody ClientRequest clienteEntradaDto) {
		try {
			Optional<ClientDto> clienteEncontrado = null;
			if (!ObjectUtils.isEmpty(clienteEntradaDto.getIdentificacion())) {
				clienteEncontrado = service.getByIdentification(clienteEntradaDto.getIdentificacion());
			} else {
				clienteEncontrado = service.getById(clienteEntradaDto.getIdCliente());
			}

			if (null != clienteEncontrado && clienteEncontrado.isPresent()) {
				ClientDto clientDto = clienteEncontrado.get();
				clientDto.setName(clienteEntradaDto.getNombre());
				clientDto.setGender(clienteEntradaDto.getGenero());
				clientDto.setAge(clienteEntradaDto.getEdad());
				clientDto.setIdentification(clienteEntradaDto.getIdentificacion());
				clientDto.setAddress(clienteEntradaDto.getDireccion());
				clientDto.setPhoneNumber(clienteEntradaDto.getTelefono());
				clientDto.setPassword(clienteEntradaDto.getContrasena());
				clientDto.setState(clienteEntradaDto.getEstado());
				ClientDto clientDtoGuardado = service.update(clientDto);
				return new ResponseEntity<ClientDto>(clientDtoGuardado, HttpStatus.OK);

			} else {
				return new ResponseEntity<>("ClientDto no se encuentra registrado", HttpStatus.CREATED);
			}
		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * <b> Metodo que elimina un registro por su id. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param id
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {

			Optional<ClientDto> personaEncontrada = service.getById(id);
			if (personaEncontrada.isPresent()) {
				service.delete(id);
				return new ResponseEntity<>("Registro Eliminado", HttpStatus.OK);
			}
			return new ResponseEntity<String>(
					"No se puede eliminar el registro con id: " + id + " no existe el registro",
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
