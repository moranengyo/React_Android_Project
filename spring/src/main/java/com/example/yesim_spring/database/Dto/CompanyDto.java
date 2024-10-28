package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.CompanyEntity;

public record CompanyDto(
        long id,
        String name,
        String email,
        String code

) {

    public static CompanyDto of(CompanyEntity entity){
        return new CompanyDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCode()
        );
    }

    public static CompanyEntity toEntity(CompanyDto dto){
        return CompanyEntity.builder()
                .id(dto.id)
                .name(dto.name)
                .email(dto.email)
                .code(dto.code)
                .build();
    }
}
