package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AuthUser;
import com.mycompany.myapp.domain.dto.CreateUserRequest;
import com.mycompany.myapp.domain.dto.ReqLoginDTO;
import com.mycompany.myapp.domain.dto.ResLoginDTO;
import com.mycompany.myapp.domain.dto.RoleDTO;
import com.mycompany.myapp.service.AuthUserService;
import com.mycompany.myapp.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        AuthUserService authUserService,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest postManUser) throws Exception {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        this.authUserService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
        );

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        AuthUser currentUserDB =
            this.authUserService.findByEmail(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại")
                );

        if (!currentUserDB.isEnabled()) {
            throw new IllegalArgumentException("Tài khoản đang bị khóa, không thể đăng nhập!");
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(currentUserDB.getId());
        roleDTO.setName(currentUserDB.getRole().getCode());

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(), roleDTO);
        res.setUser(userLogin);

        // create access token
        String access_token = jwtUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = jwtUtil.createRefreshToken(res);

        // set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
            .httpOnly(true)
            .secure(false) // local dev không HTTPS
            .path("/")
            .maxAge(100000)
            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
    }
}
