/**
 * 
 */
package ec.com.peigo.controller.payment.vo;

import lombok.Data;

/**
 * 
 * <b> Clase DTO para la entrada de los datos del cliente. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Data
public class ClientRequest {

	private String identificacion;
	private String contrasena;
	private String estado;
	private String direccion;
	private int edad;
	private String genero;
	private String nombre;
	private int telefono;
	private Long idCliente;

}
