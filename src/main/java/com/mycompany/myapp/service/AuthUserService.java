package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AuthUser;
import com.mycompany.myapp.domain.Role;
import com.mycompany.myapp.domain.dto.CreateUserRequest;
import com.mycompany.myapp.repository.AuthUserRepository;
import com.mycompany.myapp.repository.RoleRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;

    public AuthUserService(AuthUserRepository authUserRepository, RoleRepository roleRepository) {
        this.authUserRepository = authUserRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmailWithRole(email);
    }

    public void handleCreateUser(CreateUserRequest createUserRequest) throws Exception {
        AuthUser user = new AuthUser();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        Role role = this.roleRepository.findByCode("USER").orElseThrow(() -> new Exception("error"));
        user.setRole(role);
        authUserRepository.save(user);
    }
}
