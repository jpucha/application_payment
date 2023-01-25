package ec.com.peigo.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * <b> Entity class to table account. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Getter
@Setter
@DynamicUpdate
@Entity
@NamedQuery(name = "AccountDto.findAll", query = "SELECT c FROM AccountDto c")
public class AccountDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_account")
	private Long idAccount;

	@NotNull
	private String state;

	@Column(unique = true)
	@NotNull
	private int number;

	@Column(name = "balance")
	@NotNull
	private BigDecimal balance;

	@Column(name = "account_type")
	private String accountType;

	// bi-directional many-to-one association to ClientDto
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_client", insertable = false, updatable = false)
	@JsonIgnore
	private ClientDto clientDto;

	// bi-directional many-to-one association to PaymentTransactionDto
	@OneToMany(mappedBy = "accountDto")
	@JsonIgnore
	private List<PaymentTransactionDto> paymentTransactionDtos;

	@NotNull
	@Column(name = "id_client")
	private Long idClient;
}