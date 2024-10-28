package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.UserEntity;
import com.example.yesim_spring.database.entity.define.Role;
import org.apache.catalina.User;

public record UserDto(
        long seq,
        String userId,
        String userName,
        String email,
        String role
) {
    public static UserDto of(UserEntity entity){
        return new UserDto(
                entity.getSeq(),
                entity.getUserId(),
                entity.getUserNick(),
                entity.getEmail(),
                entity.getRole().toString()
        );
    }

    public static UserEntity toEntity(UserDto dto){
        return UserEntity.builder()
                .seq(dto.seq)
                .userId(dto.userId)
                .userNick(dto.userName)
                .email(dto.email)
                .role(Role.valueOf(dto.role))
                .build();
    }
}
