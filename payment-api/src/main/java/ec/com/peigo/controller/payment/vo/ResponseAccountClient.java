package ec.com.peigo.controller.payment.vo;

import ec.com.peigo.model.payment.ClientDto;
import ec.com.peigo.model.payment.AccountDto;
import lombok.Data;

import java.util.Optional;

/**
 * @author jpucha
 * Response manager class.
 */
@Data
public class ResponseAccountClient {

    private String code;

    private String errorMessage;

    private Optional<AccountDto> cuenta;

    private Optional<ClientDto> cliente;

    private Optional<AccountDto> cuentaOrigen;

    private Optional<ClientDto> clienteOrigen;

    private Optional<AccountDto> cuentaDestino;

    private Optional<ClientDto> clienteDestino;

}