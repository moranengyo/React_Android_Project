package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.ContainerEntity;

public record ContainerDto(
        long id,
        String section,
        String code
) {
    public static ContainerDto of(ContainerEntity entity) {
        return new ContainerDto(
                entity.getId(),
                entity.getSection(),
                entity.getCode()
        );
    }

    public static ContainerEntity toEntity(ContainerDto dto) {
        return ContainerEntity.builder()
                .id(dto.id())
                .section(dto.section())
                .code(dto.code())
                .build();
    }
}