package com.example.yesim_spring.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("jwt")
public class JWTProperties {
    private String issuer;
    private String secretKey;
}