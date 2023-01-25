package ec.com.peigo.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * <b> Entity class to payment transaction. </b>
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
@NamedQuery(name = "PaymentTransactionDto.findAll", query = "SELECT m FROM PaymentTransactionDto m")
public class PaymentTransactionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_payment_transaction")
	private Long idPaymentTransaction;

	@Column(name = "transaccion_number")
	private String transaccionNumber;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "before_balance")
	private BigDecimal beforeBalance;

	@NotNull
	private BigDecimal amount;

	private BigDecimal balance;

	@Column(name = "register_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;

	@Column(name = "register_user")
	private String registerUser;

	@Column(name = "modificate_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificateDate;

	@Column(name = "modificate_user")
	private String modificateUser;

	// bi-directional many-to-one association to ClientDto
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_client", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private ClientDto clientDto;

	@Column(name="id_client")
	private Long idClient;

	// bi-directional many-to-one association to AccountDto
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_account", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private AccountDto accountDto;

	@Column(name = "id_account")
	private Long idAccount;
}