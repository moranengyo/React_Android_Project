package com.example.yesim_spring.database.Dto;

public record SignUpDto(
        String userId,
        String userPw,
        String userNick,
        String email
) {

    public static SignUpDto of(
            String userId,
            String userPw,
            String userNick,
            String email
    ){
        return new SignUpDto(userId, userPw, userNick, email);
    }
}
