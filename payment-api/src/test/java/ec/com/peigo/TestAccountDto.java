/*
 * 
 *
 */
package ec.com.peigo;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.CoreMatchers.containsString;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import ec.com.peigo.enumeration.StateEmun;
import ec.com.peigo.model.payment.AccountDto;

import io.restassured.response.ValidatableResponse;

/**
 * <b> Clase test para la cuenta. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PaymentApiApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles({ "integration" })
@TestMethodOrder(OrderAnnotation.class)
public class TestAccountDto {

	public static final String TIPO_CUENTA_AHORRO = "Ahorro";

	@Value("${local.server.port}")
	private int ports;

	@BeforeAll
	public void setUp() {
		port = ports;
		baseURI = "http://localhost:8080/api/cuentas";
	}

	@Test
	@Order(1)
	public void get_obtenerCuentaPorCliente_returnsObtenerCuentaPorCliente_450() {

		ValidatableResponse response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).when().get("/1720987453").then();

		System.out.println("'get_obtenerCuentaPorCliente_returnsObtenerCuentaPorCliente_450()' response:\n"
				+ response.extract().asString());

		response.assertThat().statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
	}

	@Test
	@Order(2)
	public void post_nuevaCuenta_returnsNuevaCuenta_200() {
		
		AccountDto accountDto = new AccountDto();
		
		accountDto.setTipoCuenta(TIPO_CUENTA_AHORRO);
		accountDto.setSaldoInicial(BigDecimal.valueOf(100));
		accountDto.setState(StateEmun.ACTIVO.getDescripcion());
		accountDto.setIdCliente(1L);

		ValidatableResponse response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).body(accountDto).when().post("").then();

		System.out.println(
				"'post_nuevaCuenta_returnsNuevaCuenta_200()' response:\n" + response.extract().asString());

		response.assertThat().statusCode(HttpStatus.CREATED.value())
				.body(containsString("estado"));
	}

}
