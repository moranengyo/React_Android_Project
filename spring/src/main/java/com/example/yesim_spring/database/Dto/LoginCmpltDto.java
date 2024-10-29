package com.example.yesim_spring.database.Dto;

public record LoginCmpltDto(
        UserDto userDto,
        JwtDto jwtDto
) {
    public static LoginCmpltDto of(UserDto userDto, JwtDto jwtDto) {
        return new LoginCmpltDto(userDto, jwtDto);
    }
}
