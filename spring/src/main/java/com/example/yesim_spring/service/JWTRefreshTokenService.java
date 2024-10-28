package com.example.yesim_spring.service;

import com.example.yesim_spring.config.jwt.JWTProvider;
import com.example.yesim_spring.database.entity.RefreshTokenEntity;
import com.example.yesim_spring.database.entity.UserEntity;
import com.example.yesim_spring.database.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JWTRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTProvider provider;

    public RefreshTokenEntity makeToken(UserEntity user){
        String token = provider.makeJWT(user, Duration.ofDays(1));

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(token)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(UserEntity user) {
        refreshTokenRepository.deleteByUser(user);
    }
    public void deleteRefreshToken(String refreshToken) { refreshTokenRepository.deleteByRefreshToken(refreshToken);}

    public Optional<UserEntity> findUserByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .filter(refreshToken -> !refreshToken.isExpired())
                .map(RefreshTokenEntity::getUser);
    }

}
