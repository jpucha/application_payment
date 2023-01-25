package ec.com.peigo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class AuthorityDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAuthority;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserDto user;

}
