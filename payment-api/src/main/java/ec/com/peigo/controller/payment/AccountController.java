/**
 * 
 */
package ec.com.peigo.controller.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ec.com.peigo.model.payment.AccountDto;
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

import ec.com.peigo.controller.payment.vo.AccountRequest;
import ec.com.peigo.enumeration.StateEmun;
import ec.com.peigo.service.payment.ClientService;
import ec.com.peigo.service.payment.AccountService;

/**
 * 
 * <b> Clase controlador de las cuentas. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@RestController
@RequestMapping("/api/cuentas")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService service;

	@Autowired
	private ClientService clientService;

	/**
	 * 
	 * <b> Metodo que crea cuentas. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param cuentaEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PostMapping
	public ResponseEntity<?> create(@Validated @RequestBody AccountRequest cuentaEntradaDto) {
		try {

			Optional<ClientDto> clienteEncontrado = null;
			if (!ObjectUtils.isEmpty(cuentaEntradaDto.getIdentificacion())) {
				clienteEncontrado = clientService.getByIdentification(cuentaEntradaDto.getIdentificacion());
			} else {
				clienteEncontrado = clientService.getById(cuentaEntradaDto.getIdCliente());
			}

			if (null != clienteEncontrado && clienteEncontrado.isPresent()) {
				ClientDto clientDto = clienteEncontrado.get();
				if (StateEmun.INACTIVO.getDescripcion().equals(clientDto.getState())) {
					return new ResponseEntity<>("El clientDto se encuentra inactivo", HttpStatus.BAD_REQUEST);
				} else {
					AccountDto accountDto = new AccountDto();
					accountDto.setNumber(Integer.parseInt(cuentaEntradaDto.getNumero()));
					accountDto.setAccountType(cuentaEntradaDto.getTipoCuenta());
					accountDto.setBalance(BigDecimal.valueOf(cuentaEntradaDto.getSaldoInicial()));
					accountDto.setState(StateEmun.ACTIVO.getDescripcion());
					accountDto.setIdClient(clienteEncontrado.get().getIdClient());
					AccountDto accountDtoGuardada = service.create(accountDto);
					return new ResponseEntity<AccountDto>(accountDtoGuardada, HttpStatus.CREATED);
				}
			}
			return new ResponseEntity<>("No se encuentra registrado como cliente", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * <b> Metodo para obtiene un cliente por su identidicacion. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param cuentaEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@GetMapping
	public ResponseEntity<?> obtenerCuentaPorCliente(@Validated @RequestBody AccountRequest cuentaEntradaDto) {
		try {
			Optional<ClientDto> clienteEncontrado = null;
			if (!ObjectUtils.isEmpty(cuentaEntradaDto.getIdentificacion())) {
				clienteEncontrado = clientService.getByIdentification(cuentaEntradaDto.getIdentificacion());
			} else {
				clienteEncontrado = clientService.getById(cuentaEntradaDto.getIdCliente());
			}
			if (ObjectUtils.isEmpty(clienteEncontrado)) {
				return new ResponseEntity<>("No existe el cliente", HttpStatus.BAD_REQUEST);
			}
			List<AccountDto> listaAccountDto = service.getByClient(clienteEncontrado.get().getIdClient());
			if (null == listaAccountDto || listaAccountDto.isEmpty()) {
				return new ResponseEntity<>("No existe cuenta con el id", HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<List<AccountDto>>(listaAccountDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 
	 * <b> Metodo que actualiza la cuenta. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param cuentaEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PutMapping
	public ResponseEntity<?> update(@Validated @RequestBody AccountRequest cuentaEntradaDto) {
		try {
			if (ObjectUtils.isEmpty(cuentaEntradaDto.getIdCuenta())) {
				return new ResponseEntity<>("Debe colocar el id de la cuenta", HttpStatus.BAD_REQUEST);
			}
			Optional<AccountDto> cuenta = service.getById(cuentaEntradaDto.getIdCuenta());
			if (cuenta.isPresent()) {
				AccountDto accountDtoActualizar = cuenta.get();
				accountDtoActualizar.setNumber(Integer.parseInt(cuentaEntradaDto.getNumero()));
				accountDtoActualizar.setAccountType(cuentaEntradaDto.getTipoCuenta());
				accountDtoActualizar
						.setBalance(BigDecimal.valueOf(Double.valueOf(cuentaEntradaDto.getSaldoInicial())));
				accountDtoActualizar.setState(cuentaEntradaDto.getEstado());
				AccountDto accountDtoGuardada = service.update(accountDtoActualizar);
				return new ResponseEntity<AccountDto>(accountDtoGuardada, HttpStatus.OK);
			}
			return new ResponseEntity<>("No se encuentra la cuenta", HttpStatus.BAD_REQUEST);

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
			Optional<AccountDto> cuenta = service.getById(id);
			if (cuenta.isPresent()) {
				service.delete(id);
				return new ResponseEntity<>("Registro Eliminado", HttpStatus.OK);
			}
			return new ResponseEntity<String>("No se puede eliminar la cuenta con id: " + id + " no existe el registro",
					HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
