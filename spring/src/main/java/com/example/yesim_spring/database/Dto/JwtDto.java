package com.example.yesim_spring.database.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private String accessToken;
    private String refreshToken;

}
