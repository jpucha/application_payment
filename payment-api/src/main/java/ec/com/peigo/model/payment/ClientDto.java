package ec.com.peigo.model.payment;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * <b> Entity class to table client. </b>
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
@NamedQuery(name = "ClientDto.findAll", query = "SELECT c FROM ClientDto c")
public class ClientDto extends PersonDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_client")
	private Long idClient;

	private String password;

	private String state;

	@OneToMany(mappedBy = "clientDto")
	@JsonIgnore
	private List<AccountDto> accountDtos;

	@OneToMany(mappedBy = "clientDto")
	@JsonIgnore
	private List<PaymentTransactionDto> paymentTransactionDtos;
}