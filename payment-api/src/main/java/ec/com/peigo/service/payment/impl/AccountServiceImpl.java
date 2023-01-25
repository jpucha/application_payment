/**
 * 
 */
package ec.com.peigo.service.payment.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.peigo.model.payment.AccountDto;
import ec.com.peigo.repository.payment.AccountRepository;
import ec.com.peigo.service.payment.AccountService;

/**
 * 
 * <b> Servicio para la AccountDto. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public AccountDto create(AccountDto accountDto) {
		return accountRepository.save(accountDto);
	}

	@Override
	public List<AccountDto> read() {
		return accountRepository.findAll();
	}

	@Override
	public AccountDto update(AccountDto accountDto) {
		return accountRepository.save(accountDto);
	}

	@Override
	public void delete(Long id) {
		accountRepository.deleteById(id);

	}

	@Override
	public Optional<AccountDto> getById(Long id) {
		return accountRepository.findById(id);
	}

	@Override
	public List<AccountDto> getByClient(Long id) {
		return accountRepository.findByIdClient(id);
	}

	@Override
	public Optional<AccountDto> getByAccountNumber(int accountNumber) {
		return accountRepository.findByNumber(accountNumber);
	}

}
