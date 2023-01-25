/*
 *  
 * 
 */
package ec.com.peigo;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.CoreMatchers.containsString;

import ec.com.peigo.PaymentApiApplicationTests;
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

import io.restassured.response.ValidatableResponse;

/**
 * <b> Clase para realizar los test del reporte. </b>
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
public class TestReporte {

	@Value("${local.server.port}")
	private int ports;

	@BeforeAll
	public void setUp() {
		port = ports;
		baseURI = "http://localhost:8080/api/reportes";
	}

	@Test
	@Order(1)
	public void get_AllMovimientosClienteCuenta_returnsAllMovimientos_200() {

		ValidatableResponse response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).when().get("?fecha=2023-01-21,2023-01-22").then();

		System.out.println("'get_AllMovimientosClienteCuenta_returnsAllMovimientos_200()' response:\n"
				+ response.extract().asString());

		response.assertThat().statusCode(HttpStatus.OK.value()).body(containsString("2006789453"));
	}

	@Test
	@Order(2)
	public void get_MovimientosSinFecha_returnsErroInterno_500() {

		ValidatableResponse response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).when().get("?fecha=").then();

		System.out.println(
				"'get_MovimientosSinFecha_returnsErroInterno_500()' response:\n" + response.extract().asString());

		response.assertThat().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(containsString("Fechas Obligatorias"));
	}

}
