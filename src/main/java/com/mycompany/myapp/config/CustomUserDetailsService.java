package com.mycompany.myapp.config;

import com.mycompany.myapp.domain.AuthUser;
import com.mycompany.myapp.repository.AuthUserRepository;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    public CustomUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = authUserRepository
            .findByEmailWithRole(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(authority)
        );
    }
}
