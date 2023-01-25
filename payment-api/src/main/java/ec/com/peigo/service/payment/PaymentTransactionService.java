/**
 * 
 */
package ec.com.peigo.service.payment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.peigo.controller.payment.vo.PaymentRequest;
import ec.com.peigo.controller.payment.vo.ResponseAccountClient;
import ec.com.peigo.model.payment.PaymentTransactionDto;
import org.springframework.http.ResponseEntity;

/**
 * 
 * <b> Interfaz del servicio para el PaymentTransactionDto. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface PaymentTransactionService {
	/**
	 * Create payment transaction.
	 * @param paymentTransactionDto
	 * @return
	 */
	PaymentTransactionDto create(PaymentTransactionDto paymentTransactionDto);

	/**
	 * Read payment transaction.
	 * @return
	 */
	List<PaymentTransactionDto> read();

	/**
	 * Update payment transaction.
	 * @param paymentTransactionDto
	 * @return
	 */
	PaymentTransactionDto update(PaymentTransactionDto paymentTransactionDto);

	/**
	 * Delete payment transaction.
	 * @param id
	 */
	void delete(Long id);

	/**
	 * Get payment transaction by id.
	 * @param id
	 * @return
	 */
	Optional<PaymentTransactionDto> getById(Long id);

	/**
	 *
	 * @param idClient
	 * @param idAccount
	 * @return
	 */
	List<PaymentTransactionDto> getByClientAccount(Long idClient, Long idAccount);

	/**
	 *
	 * @param identification
	 * @param accountNumber
	 * @return
	 */
	List<PaymentTransactionDto> getByIdentificationAccountNumber(String identification, int accountNumber);

	/**
	 *
	 * @param idClient
	 * @param idAccount
	 * @param transactionType
	 * @param registerDate
	 * @return
	 */
	Double getSumAmountByClientAccountDate(Long idClient, Long idAccount, String transactionType, Date registerDate);

	/**
	 * @autor jpucha
	 * Create payment transaction.
	 * */
	ResponseEntity<?> createPaymentTransaction(ResponseAccountClient responseDto, PaymentRequest paymentInDto, String transactionNumber) throws Exception;

}
