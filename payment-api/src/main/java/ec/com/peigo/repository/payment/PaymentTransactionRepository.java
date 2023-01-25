/**
 * 
 */
package ec.com.peigo.repository.payment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ec.com.peigo.model.payment.PaymentTransactionDto;

/**
 * 
 * <b> Payment transaction interface. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionDto, Long> {

	List<PaymentTransactionDto> findByIdClientAndIdAccount(Long idClient, Long idAccount);

	@Query(value = "SELECT c FROM PaymentTransactionDto c WHERE c.clientDto.identification= :identification AND c.accountDto.number= :number")
	List<PaymentTransactionDto> getByClientAccount(String identification, int number);

	@Query(value = "SELECT COALESCE(SUM(c.amount),0) FROM PaymentTransactionDto c WHERE c.clientDto.idClient= :idClient AND c.accountDto.idAccount= :idAccount AND transactionType= :transactionType AND CONVERT(c.registerDate, DATE) = CONVERT(:registerDate, DATE)")
	Double getSumAmountByClientAccountDate(Long idClient, Long idAccount, String transactionType, Date registerDate);


}
