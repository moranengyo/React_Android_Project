package com.example.yesim_spring.config.jwt;

import com.example.yesim_spring.database.entity.UserEntity;
import com.example.yesim_spring.database.entity.define.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;


// Token 생성 및 겸증
@Service
@RequiredArgsConstructor
public class JWTProvider {
    private final JWTProperties properties;

    public String makeJWT(UserEntity user, Duration expiredTime){
        Date now = new Date();

        return makeJWT(user, new Date(now.getTime() + expiredTime.toMillis()));
    }

    public String makeJWT(UserEntity user, Date expiredTime){
        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getEmail());
        claims.put("userNick", user.getUserNick());
        claims.put("userRole", user.getRole());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE,  Header.JWT_TYPE)
                .setIssuer(properties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .setSubject(user.getEmail())
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey())
                .compact();
    }

    public boolean checkJWT(String token){
        try{
            Jwts.parser()
                    .setSigningKey(properties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(properties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthInfo(String token){
        Claims claims = this.parseClaims(token);
        Set<SimpleGrantedAuthority> grantedAuthSet =
                Collections.singleton(
                        new SimpleGrantedAuthority(claims.get("userRole").toString())
                );

        UserEntity user = UserEntity.builder()
                .userId(claims.get("userId").toString())
                .email(claims.get("email").toString())
                .userNick(claims.get("userNick").toString())
                .role(Role.valueOf(claims.get("userRole").toString()))
                .build();


        return new UsernamePasswordAuthenticationToken(user, token, grantedAuthSet);
    }
}