package ec.com.peigo.controller.payment.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private Integer cuentaOrigen;

    private Integer cuentaDestino;

    private BigDecimal monto;
}
