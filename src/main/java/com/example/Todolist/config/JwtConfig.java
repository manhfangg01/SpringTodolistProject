package com.example.Todolist.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class JwtConfig {

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtKey;

    /**
     * Bean dùng để mã hóa JWT (tạo token)
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtKey);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    /**
     * Bean dùng để giải mã JWT (xác thực token)
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtKey);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}