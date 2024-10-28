package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.RefreshTokenEntity;
import com.example.yesim_spring.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    void deleteByUser(UserEntity user);
    void deleteByRefreshToken(String refreshToken);
}
