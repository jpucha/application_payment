/**
 * 
 */
package ec.com.peigo;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.peigo.controller.payment.vo.ClientRequest;

/**
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
    @WithMockUser(username = "felord", password = "felord.cn", roles = {"ADMIN"})
    public void post_createNewClient_Returns_201_Created() throws Exception {

        ClientRequest cliente = new ClientRequest();
        cliente.setIdCliente(1L);
        cliente.setNombre("Susana Gonzalez");
        cliente.setGenero("F");
        cliente.setEdad(54);
        cliente.setIdentificacion("1712312312");
        cliente.setDireccion("Av. Colon y Av. 6 de diciembre");
        cliente.setTelefono(909090909);
        cliente.setContrasena("1234");
        cliente.setEstado(Boolean.TRUE.toString());
        mvc.perform(MockMvcRequestBuilders.post("/api/clientes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(cliente))).andExpect(status().isCreated());
    }

}
