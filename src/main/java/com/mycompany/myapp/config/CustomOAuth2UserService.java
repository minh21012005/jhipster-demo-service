package com.mycompany.myapp.config;

import com.mycompany.myapp.domain.AuthUser;
import com.mycompany.myapp.service.AuthUserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AuthUserService authUserService;

    public CustomOAuth2UserService(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Lấy hoặc tạo user trong DB
        AuthUser user = authUserService.findByEmail(email).orElseGet(() -> authUserService.registerOAuthUser(email));

        // Map role để cấp quyền
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("role", user.getRole().getCode());

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode())),
            attributes,
            "email"
        );
    }
}
