/**
 * 
 */
package ec.com.peigo;

import ec.com.peigo.model.payment.ClientDto;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 
 * <b> Clase test para el cliente. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PaymentApiApplicationTests.class)
@TestMethodOrder(OrderAnnotation.class)
public class TestClientDto {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@Order(1)
	public void post_createNewClient_Returns_201_Created() throws JsonProcessingException {
		ClientDto clientDto = new ClientDto();
		clientDto.setClienteId(1L);
		clientDto.setNombre("Susana Gonzalez");
		clientDto.setGenero("F");
		clientDto.setEdad(54);
		clientDto.setIdentificacion("1712312312");
		clientDto.setDireccion("Av. Colon y Av. 6 de diciembre");
		clientDto.setTelefono(909090909);
		clientDto.setContrasena("1234");
		clientDto.setEstado(Boolean.TRUE.toString());
		HttpEntity<String> entity = getStringHttpEntity(clientDto);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/clientes", entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HttpEntity<String> getStringHttpEntity(Object object) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String jsonCliente = mapper.writeValueAsString(object);
		return (HttpEntity<String>) new HttpEntity(jsonCliente, headers);
	}

}
