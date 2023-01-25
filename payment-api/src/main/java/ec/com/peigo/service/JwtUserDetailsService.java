package ec.com.peigo.service;

import java.util.*;

import ec.com.peigo.controller.payment.vo.UserRequest;
import ec.com.peigo.model.*;
import ec.com.peigo.repository.payment.AuthorityRepository;
import ec.com.peigo.repository.payment.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private AuthorityRepository autorityDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<AuthorityDto> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (AuthorityDto authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }
    public UserDto save(UserRequest user) {
        UserDto newUser = new UserDto();
        AuthorityDto rol = new AuthorityDto();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser = userDao.save(newUser);
        rol.setName(user.getAuthorities().iterator().next().getName());
        rol.setUser(newUser);
        autorityDao.save(rol);
        Set<AuthorityDto>  rolSet = new HashSet<AuthorityDto>();
        rolSet.add(rol);
        newUser.setAuthorities(rolSet);
        return newUser;
    }
}
