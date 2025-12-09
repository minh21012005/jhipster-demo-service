package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AuthUser;
import com.mycompany.myapp.domain.Role;
import com.mycompany.myapp.domain.dto.CreateUserRequest;
import com.mycompany.myapp.repository.AuthUserRepository;
import com.mycompany.myapp.repository.RoleRepository;
import com.mycompany.myapp.web.rest.errors.IdInvalidException;
import java.util.Optional;
import java.util.UUID;
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

    public void handleCreateUser(CreateUserRequest createUserRequest) throws IdInvalidException {
        AuthUser user = new AuthUser();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        Role role = this.roleRepository.findByCode("USER").orElseThrow(() -> new IdInvalidException("error"));
        user.setRole(role);
        authUserRepository.save(user);
    }

    public Optional<AuthUser> findById(UUID id) {
        return authUserRepository.findByIdWithRole(id);
    }

    public AuthUser registerOAuthUser(String email) {
        // Kiểm tra nếu user đã tồn tại
        Optional<AuthUser> existingUser = authUserRepository.findByEmailWithRole(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        AuthUser user = new AuthUser();
        user.setEmail(email);

        // Không cần password, hoặc có thể generate ngẫu nhiên
        user.setPassword(UUID.randomUUID().toString());

        // Gán role mặc định
        Role role = roleRepository.findByCode("USER").orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
        user.setRole(role);

        // User OAuth mặc định enable = true
        user.setEnabled(true);

        // Lưu vào DB
        return authUserRepository.save(user);
    }
}
