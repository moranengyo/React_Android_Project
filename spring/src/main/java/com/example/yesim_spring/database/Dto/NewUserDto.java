package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.UserEntity;

public record NewUserDto(
        long seq,
        String userId,
        String userName,
        String email,
        String role
) {
    public static NewUserDto of(long seq, String userId, String userName, String email, String role) {
        return new NewUserDto(seq, userId, userName, email, role);
    }

    public static NewUserDto of(UserEntity user) {
        return new NewUserDto(
                user.getSeq(),
                user.getUserId(),
                user.getUserNick(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}
