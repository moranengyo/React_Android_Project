package com.example.yesim_spring.database.Dto;

public record LoginDto(
        String userId,
        String userPw
) {
    public static LoginDto of(String userId, String userPw) {
        return new LoginDto(userId, userPw);
    }
}
