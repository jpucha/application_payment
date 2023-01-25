/**
 * 
 */
package ec.com.peigo.service.payment;

import java.util.List;
import java.util.Optional;

import ec.com.peigo.model.payment.AccountDto;

/**
 * 
 * <b> Interfaz del servicio para el AccountDto. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
public interface AccountService {
	AccountDto create(AccountDto accountDto);

	List<AccountDto> read();

	AccountDto update(AccountDto accountDto);

	void delete(Long id);

	Optional<AccountDto> getById(Long id);

	List<AccountDto> getByClient(Long id);

	Optional<AccountDto> getByAccountNumber(int accountNumber);

}
