/**
 * 
 */
package ec.com.peigo.service.payment.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.peigo.controller.payment.vo.PaymentRequest;
import ec.com.peigo.controller.payment.vo.PaymentResponse;
import ec.com.peigo.controller.payment.vo.ResponseAccountClient;
import ec.com.peigo.enumeration.TransactionTypeEnum;
import ec.com.peigo.model.payment.ClientDto;
import ec.com.peigo.model.payment.AccountDto;
import ec.com.peigo.model.payment.PaymentTransactionDto;
import ec.com.peigo.service.payment.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ec.com.peigo.repository.payment.PaymentTransactionRepository;
import ec.com.peigo.service.payment.PaymentTransactionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <b> Servicio para el PaymentTransactionDto. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

	private static final Logger log = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

	public static final String USER_MOD = "peigoadmin";

	@Autowired
	private PaymentTransactionRepository paymentTransactionRepository;

	@Autowired
	private AccountService accountService;

	@Override
	public PaymentTransactionDto create(PaymentTransactionDto paymentTransactionDto) {

		return paymentTransactionRepository.save(paymentTransactionDto);
	}

	@Override
	public List<PaymentTransactionDto> read() {

		return paymentTransactionRepository.findAll();
	}

	@Override
	public PaymentTransactionDto update(PaymentTransactionDto paymentTransactionDto) {
		return paymentTransactionRepository.save(paymentTransactionDto);
	}

	@Override
	public void delete(Long id) {
		paymentTransactionRepository.deleteById(id);

	}

	@Override
	public Optional<PaymentTransactionDto> getById(Long id) {

		return paymentTransactionRepository.findById(id);
	}

	@Override
	public List<PaymentTransactionDto> getByClientAccount(Long idCliente, Long idCuenta) {
		return paymentTransactionRepository.findByIdClientAndIdAccount(idCliente, idCuenta);
	}

	@Override
	public List<PaymentTransactionDto> getByIdentificationAccountNumber(String identificacion, int numeroCuenta) {
		return paymentTransactionRepository.getByClientAccount(identificacion, numeroCuenta);
	}

	@Override
	public Double getSumAmountByClientAccountDate(Long idClient, Long idAccount, String transactionType, Date registerDate) {
		return paymentTransactionRepository.getSumAmountByClientAccountDate(idClient, idAccount, transactionType, registerDate);
	}

	@Transactional
	@Override
	public ResponseEntity<?> createPaymentTransaction(ResponseAccountClient responseDto, PaymentRequest paymentInDto, String transactionNumber ) throws Exception {
		try {
			//Create debit transaction
			PaymentTransactionDto debit = createTransaction(responseDto.getCuentaOrigen(), responseDto.getClienteOrigen(),
					paymentInDto.getMonto(), TransactionTypeEnum.DEBITO.getDescripcion(), transactionNumber);
			//Update origin account
			responseDto.getCuentaOrigen().get().setBalance(debit.getBalance());
			accountService.update(responseDto.getCuentaOrigen().get());
			//Create credit transaction
			PaymentTransactionDto credit = createTransaction(responseDto.getCuentaDestino(), responseDto.getClienteDestino(),
					paymentInDto.getMonto(), TransactionTypeEnum.CREDITO.getDescripcion(), transactionNumber);
			//Update destination account
			responseDto.getCuentaDestino().get().setBalance(credit.getBalance());
			accountService.update(responseDto.getCuentaDestino().get());
			//result
			PaymentResponse result = new PaymentResponse();
			result.setNumeroOperacion(transactionNumber);
			result.setSaldoCuentaOrigen(debit.getBalance());
			result.setSaldoCuentaDestino(credit.getBalance());
			return new ResponseEntity<PaymentResponse>(result, HttpStatus.CREATED);
		}catch (Exception e){
			log.error("Error: createPaymentTransaction: ", e);
			throw new Exception(e);
		}

	}

	/**
	 * Create debit transaction.
	 * @param cuenta, Account.
	 * @param cliente, Client.
	 * @param valor, Value to subtract from the origen client balance.
	 * @param tipoMovimiento, Transaction type.
	 * @return movimiento object.
	 */
	private PaymentTransactionDto createTransaction (Optional<AccountDto> cuenta, Optional<ClientDto> cliente, BigDecimal valor,
													 String tipoMovimiento, String transactionNumber) throws Exception {
		PaymentTransactionDto transaction = new PaymentTransactionDto();
		try{

			transaction.setIdClient(cuenta.get().getIdClient());
			transaction.setIdAccount(cuenta.get().getIdAccount());
			transaction.setTransaccionNumber(transactionNumber);
			transaction.setAmount(valor);
			transaction.setTransactionType(tipoMovimiento);
			transaction.setBeforeBalance(cuenta.get().getBalance());
			if(StringUtils.equalsAnyIgnoreCase(TransactionTypeEnum.DEBITO.getDescripcion(),tipoMovimiento) ){
				transaction.setBalance(transaction.getBeforeBalance().subtract(transaction.getAmount()));
			}else{
				transaction.setBalance(transaction.getBeforeBalance().add(transaction.getAmount()));
			}
			transaction.setRegisterDate(new Date());
			transaction.setRegisterUser(USER_MOD);
			//save debito
			transaction = create(transaction);
			return transaction;
		}catch (Exception e){
			log.error("Error: createTransaction: " + tipoMovimiento, e);
			throw new Exception(e);
		}
	}
}
