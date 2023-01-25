/**
 * 
 */
package ec.com.peigo.controller.payment.vo;

import lombok.Data;

/**
 * 
 * <b> Clase Dto para la entrada del movimiento. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Data
public class TransactionRequest {

	private Long idMovimiento;
	private double saldo;
	private String tipoMovimiento;
	private String fecha;
	private Long idCliente;
	private Long idCuenta;
	private double valor;
	private String identificacion;
	private int numeroCuenta;
}
