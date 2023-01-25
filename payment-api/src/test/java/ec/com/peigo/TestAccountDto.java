/*
 * 
 *
 */
package ec.com.peigo;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.peigo.controller.payment.vo.AccountRequest;

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
@ActiveProfiles({"integration"})
@TestMethodOrder(OrderAnnotation.class)
public class TestAccountDto {
    
    public static final String TIPO_CUENTA_AHORRO = "Ahorro";

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "felord", password = "felord.cn", roles = {"CUSTOMER"})
    public void post_nuevaCuenta_returnsUserNoRegistrado_400() throws Exception {

        AccountRequest cuenta = new AccountRequest();

        cuenta.setTipoCuenta(TIPO_CUENTA_AHORRO);
        cuenta.setIdentificacion("1712312312");
        cuenta.setSaldoInicial(BigDecimal.valueOf(100).doubleValue());
        cuenta.setNumero("123456789");
        cuenta.setIdCliente(1L);

        mvc.perform(MockMvcRequestBuilders.post("/api/cuentas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(cuenta))).andExpect(status().isBadRequest());
    }

}
