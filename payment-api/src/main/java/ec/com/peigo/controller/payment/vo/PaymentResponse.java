package ec.com.peigo.controller.payment.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponse {

    private String numeroOperacion;

    private BigDecimal saldoCuentaOrigen;

    private BigDecimal saldoCuentaDestino;


}
