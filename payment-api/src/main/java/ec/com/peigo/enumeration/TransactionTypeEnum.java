/**
 * 
 */
package ec.com.peigo.enumeration;

import lombok.Getter;

/**
 * 
 * <b> Enumeracion del tipo de movimiento. </b>
 * 
 * @author jpucha
 * @version $Revision: 1.0 $
 *          <p>
 *          [$Author: jpucha $, $Date: 22 ene. 2023 $]
 *          </p>
 */
@Getter
public enum TransactionTypeEnum {

	CREDITO("CRE", "credito"), DEBITO("DEB", "debito");

	private String codigo;
	private String descripcion;

	private TransactionTypeEnum(String codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
}
