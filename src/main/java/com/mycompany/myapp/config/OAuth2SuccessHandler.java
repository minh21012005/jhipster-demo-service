package com.mycompany.myapp.config;

import com.mycompany.myapp.domain.dto.ResLoginDTO;
import com.mycompany.myapp.domain.dto.RoleDTO;
import com.mycompany.myapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAttribute("role");

        // Tạo JWT
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role);
        resLoginDTO.setUser(new ResLoginDTO.UserLogin("", email, roleDTO));

        String jwtToken = jwtUtil.createAccessToken(email, resLoginDTO);
        String refreshToken = jwtUtil.createRefreshToken(resLoginDTO);

        // Gửi token về FE (có thể header hoặc redirect)
        response.sendRedirect("http://localhost:3000/oauth-success?token=" + jwtToken);
    }
}
