/**
 * 
 */
package ec.com.peigo.controller.payment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import ec.com.peigo.controller.payment.vo.PaymentRequest;
import ec.com.peigo.controller.payment.vo.ResponseAccountClient;
import ec.com.peigo.model.payment.PaymentTransactionDto;
import org.apache.commons.lang3.StringUtils;
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

import ec.com.peigo.controller.payment.vo.TransactionRequest;
import ec.com.peigo.enumeration.TransactionTypeEnum;
import ec.com.peigo.model.payment.ClientDto;
import ec.com.peigo.model.payment.AccountDto;
import ec.com.peigo.service.payment.ClientService;
import ec.com.peigo.service.payment.AccountService;
import ec.com.peigo.service.payment.PaymentTransactionService;

/**
 * 
 * <b> Clase controlador para los movimientos contables. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@RestController
@RequestMapping("/api/paymentTransaction")
public class PaymentController {

	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

	public static final String USER_MOD = "peigoadmin2";

	private static final int limiteDiario = 1000;

	@Autowired
	private PaymentTransactionService paymentTransactionService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private AccountService accountService;

	/**
	 * 
	 * <b> Metodo que crea los movimientos. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param paymentInDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PostMapping
	public ResponseEntity<?> create(@Validated @RequestBody PaymentRequest paymentInDto) {

		try {
			ResponseAccountClient responseAccountClient = new ResponseAccountClient();
			//validations
			if(validate(paymentInDto, responseAccountClient)){
				//Get transaction number.
				String transactionNumber = getTransactionNumber(responseAccountClient.getCuentaOrigen().get().getNumber());
				return paymentTransactionService.createPaymentTransaction(responseAccountClient,paymentInDto,transactionNumber);
			}
			return new ResponseEntity<>(responseAccountClient.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador. ", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Validate data request.
	 * @param paymentInDto, request data.
	 * @return ResponseDto, Response data.
	 */
	private ResponseAccountClient validateDataIn (PaymentRequest paymentInDto)  {
		ResponseAccountClient response = new ResponseAccountClient();
		//validacion que los datos de entrada de cuenta origen y cuenta destino no sean vacios o nulos
		if (ObjectUtils.isEmpty(paymentInDto.getCuentaOrigen()) || ObjectUtils.isEmpty(paymentInDto.getCuentaDestino())) {
			response.setErrorMessage("AccountDto origen, cuenta destino, monto son obligatorios.");
			return response;
		}
		//validacion que la cuenta de origen no sea la misma de destino
		if(StringUtils.equalsAnyIgnoreCase(paymentInDto.getCuentaOrigen().toString(),paymentInDto.getCuentaDestino().toString())){
			response.setErrorMessage("No se puede realizar transacciones entre la misma cuenta.");
			return response;
		}
		// validacion que el monto enviado en los datos de entrada no sea nulo
		if (ObjectUtils.isEmpty(paymentInDto.getMonto())) {
			response.setErrorMessage("Monto no existe.");
			return response;
		}
		// validacion que el monto enviado en los datos de entrada no sean cero y que sea positivo
		if (paymentInDto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
			response.setErrorMessage("Monto no puede ser cero o negativo.");
			return response;
		}
		return response;
	}

	/**
	 * Validate Source Account.
	 * @param paymentInDto, data in.
	 * @return ResponseDto, response object.
	 */
	private ResponseAccountClient validateSourceAccount(PaymentRequest paymentInDto){
		ResponseAccountClient response = new ResponseAccountClient();
		Optional<AccountDto> cuentaOrigen = accountService.getByAccountNumber(paymentInDto.getCuentaOrigen());
		if (!cuentaOrigen.isPresent()) {
			response.setErrorMessage("La cuenta origen proporcionada no existe.");
			return response;
		}else{
			response.setCuenta(cuentaOrigen);
		}
		//Valido que la cuenta origen tenga saldo
		if (BigDecimal.ZERO.compareTo(cuentaOrigen.get().getBalance()) == 0) {
			response.setErrorMessage("Saldo no disponible en la cuenta origen");
			return response;
		}
		//valido que el movimiento a debitar sea menor o igual que el saldo que se tiene en la cuenta
		if (paymentInDto.getMonto().compareTo(cuentaOrigen.get().getBalance()) == 1) {
			response.setErrorMessage("Saldo no disponible");
			return response;
		}

		//Validacion que este dentro del limite diario
		Double sumaDiariaDebito = paymentTransactionService.getSumAmountByClientAccountDate(cuentaOrigen.get().getIdClient(),
				cuentaOrigen.get().getIdAccount(), TransactionTypeEnum.DEBITO.getDescripcion(), new Date());
		if (limiteDiario <= sumaDiariaDebito.doubleValue() + paymentInDto.getMonto().doubleValue()) {
			response.setErrorMessage("Cupo diario excedido");
			return response;
		}

		Optional<ClientDto> clienteOrigen = clientService.getById(cuentaOrigen.get().getIdClient());
		if (!clienteOrigen.isPresent()) {
			response.setErrorMessage("El cliente origen no existe para el id de cuenta consultado.");
			return response;
		}else{
			response.setCliente(clienteOrigen);
		}
		return response;
	}

	/**
	 * Validate Destination Account.
	 * @param paymentInDto, Data in.
	 * @return ResponseDto, Response Data.
	 */
	private ResponseAccountClient validateDestinationAccount(PaymentRequest paymentInDto){
		ResponseAccountClient response = new ResponseAccountClient();
		Optional<AccountDto> cuentaDestino = accountService.getByAccountNumber(paymentInDto.getCuentaDestino());
		if (!cuentaDestino.isPresent()) {
			response.setErrorMessage("La cuenta destino proporcionada no existe.");
			return response;
		}else {
			response.setCuenta(cuentaDestino);
		}
		Optional<ClientDto> clienteDestino = clientService.getById(cuentaDestino.get().getIdClient());
		if (!clienteDestino.isPresent()) {
			response.setErrorMessage("El cliente destino no existe para el id de cuenta consultado.");
			return response;
		}else{
			response.setCliente(clienteDestino);
		}
		return response;
	}

	/**
	 * Transaction number is today(yyyyMMddHHmmss) date and concat origin account number.
	 * @param numberAccount, number account.
	 * @return String, transaccion number.
	 */
	private String getTransactionNumber (Integer numberAccount){
		LocalDateTime today = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return today.format(format).concat(numberAccount.toString());
	}

	private Boolean validate(PaymentRequest paymentInDto, ResponseAccountClient responseDto){
		ResponseAccountClient response = validateDataIn(paymentInDto);
		//Validate data in
		String message = response.getErrorMessage();
		if(StringUtils.isNotEmpty(message)){
			responseDto.setErrorMessage(message);
			return Boolean.FALSE;
		}
		//Validate Source Account
		response = validateSourceAccount(paymentInDto);
		message = response.getErrorMessage();
		if(StringUtils.isNotEmpty(message)){
			responseDto.setErrorMessage(message);
			return Boolean.FALSE;
		}
		responseDto.setCuentaOrigen(response.getCuenta());
		responseDto.setClienteOrigen(response.getCliente());
		//Validate Destination Account
		response = validateDestinationAccount(paymentInDto);
		message = response.getErrorMessage();
		if(StringUtils.isNotEmpty(message)){
			responseDto.setErrorMessage(message);
			return Boolean.FALSE;
		}
		responseDto.setCuentaDestino(response.getCuenta());
		responseDto.setClienteDestino(response.getCliente());
		return Boolean.TRUE;
	}

	/**
	 * 
	 * <b> Metodo para obtiene los movimientos del cliente por su cuenta. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param movimientoEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity lista o mensaje de error
	 */
	@GetMapping
	public ResponseEntity<?> obtenerPorClienteCuenta(
			@Validated @RequestBody TransactionRequest movimientoEntradaDto) {
		try {

			if (ObjectUtils.isEmpty(movimientoEntradaDto.getIdentificacion())
					|| ObjectUtils.isEmpty(movimientoEntradaDto.getNumeroCuenta())) {
				return new ResponseEntity<>("La identificación y el número de cuenta son obligatorios",
						HttpStatus.BAD_REQUEST);
			}
			Optional<ClientDto> cliente = clientService
					.getByIdentification(movimientoEntradaDto.getIdentificacion());
			if (ObjectUtils.isEmpty(cliente)) {
				return new ResponseEntity<>("El cliente no existe con la identificación", HttpStatus.BAD_REQUEST);
			}
			Optional<AccountDto> cuenta = accountService.getByAccountNumber(movimientoEntradaDto.getNumeroCuenta());
			if (ObjectUtils.isEmpty(cuenta)) {
				return new ResponseEntity<>("La cuenta no existe con el número de cuenta", HttpStatus.BAD_REQUEST);
			}

			List<PaymentTransactionDto> listadoPaymentTransactionDto = paymentTransactionService.getByClientAccount(cliente.get()
							.getIdClient(),
					cuenta.get().getIdAccount());
			if (null == listadoPaymentTransactionDto || listadoPaymentTransactionDto.isEmpty()) {
				return new ResponseEntity<>("No existen movimientos con lo parametros indicados.", HttpStatus.OK);
			} else {
				return new ResponseEntity<List<PaymentTransactionDto>>(listadoPaymentTransactionDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * <b> Metodo que actualiza el moviemto. </b>
	 * <p>
	 * [Author: jpucha, Date: 22 ene. 2023]
	 * </p>
	 *
	 * @param movimientoEntradaDto
	 *            parametro de entrada
	 * @return ResponseEntity<?> lista o mensaje de error
	 */
	@PutMapping
	public ResponseEntity<?> update(@Validated @RequestBody TransactionRequest movimientoEntradaDto) {
		try {
			if (ObjectUtils.isEmpty(movimientoEntradaDto.getIdMovimiento())) {
				return new ResponseEntity<>("El id del movimiento es obligatorio", HttpStatus.BAD_REQUEST);
			}
			Optional<PaymentTransactionDto> movimientoEncontrado = paymentTransactionService.getById(movimientoEntradaDto.getIdMovimiento());
			if (movimientoEncontrado.isPresent()) {
				PaymentTransactionDto paymentTransactionDto = movimientoEncontrado.get();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				Date date = formatter.parse(movimientoEntradaDto.getFecha());
				paymentTransactionDto.setModificateDate(new Date());
				paymentTransactionDto.setModificateUser(USER_MOD);
				PaymentTransactionDto paymentTransactionDtoGuardado = paymentTransactionService.update(paymentTransactionDto);
				return new ResponseEntity<PaymentTransactionDto>(paymentTransactionDtoGuardado, HttpStatus.OK);
			}
			return new ResponseEntity<>("No se encuentra en movimiento", HttpStatus.BAD_REQUEST);

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

			if (ObjectUtils.isEmpty(id)) {
				return new ResponseEntity<>("El id del movimiento es obligatorio", HttpStatus.BAD_REQUEST);
			}
			Optional<PaymentTransactionDto> movimientoEncontrado = paymentTransactionService.getById(id);
			if (movimientoEncontrado.isPresent()) {
				paymentTransactionService.delete(id);
				return new ResponseEntity<>("Registro Eliminado", HttpStatus.OK);
			}
			return new ResponseEntity<String>(
					"No se puede eliminar el movimiento con id: " + id + " no existe el registro",
					HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			log.error("Por favor comuniquese con el administrador", e);
			return new ResponseEntity<>("Por favor comuniquese con el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
