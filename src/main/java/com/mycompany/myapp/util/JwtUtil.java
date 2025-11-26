package com.mycompany.myapp.util;

import com.mycompany.myapp.domain.dto.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final JwtEncoder jwtEncoder;

    public JwtUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    public String createAccessToken(String email, ResLoginDTO dto) {
        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setRole(dto.getUser().getRole().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(100000, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(String.valueOf(dto.getUser().getId()))
            .claim("email", userToken.getEmail())
            .claim("role", userToken.getRole())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(ResLoginDTO dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(100000, ChronoUnit.SECONDS);

        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setRole(dto.getUser().getRole().getName());
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(String.valueOf(dto.getUser().getId()))
            .claim("email", userToken.getEmail())
            .claim("role", userToken.getRole())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
            JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) throws Exception{
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
            getSecretKey()).macAlgorithm(JwtUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new Exception("Refresh Token không hợp lệ hoặc đã hết hạn");
        }
    }
}
