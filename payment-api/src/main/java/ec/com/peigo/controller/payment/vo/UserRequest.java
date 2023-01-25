package ec.com.peigo.controller.payment.vo;

import ec.com.peigo.model.AuthorityDto;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    private String username;
    private String password;
    private Set<AuthorityDto> authorities;
}
