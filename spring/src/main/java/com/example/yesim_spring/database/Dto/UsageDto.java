package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.*;
import com.example.yesim_spring.database.repository.UsageRepository;

import java.awt.*;
import java.lang.reflect.GenericDeclaration;
import java.time.LocalDateTime;

public record UsageDto(
        long id,
        int usageNum,
        LocalDateTime usageTime,
        ItemDto item,
        UserDto user

) {
    public static UsageDto of(UsageEntity entity) {
        return new UsageDto(
                entity.getId(),
                entity.getUsageNum(),
                entity.getUsageTime(),
                ItemDto.of(entity.getItem()),
                UserDto.of(entity.getUser())
        );
    }

    public static UsageDto of(UsageRepository.interItemUsageDto itemUsageDto) {
        return new UsageDto(
                -1,
                itemUsageDto.getTotalUsageNum(),
                null,
                ItemDto.of(
                        ItemEntity.builder()
                                .id(itemUsageDto.getItemId())
                                .name(itemUsageDto.getItemName())
                                .company(
                                        CompanyEntity.builder()
                                                .name(itemUsageDto.getCompanyName())
                                                .build())
                                .container(
                                        ContainerEntity.builder()
                                                .section(itemUsageDto.getContainerSection())
                                                .build()
                                )
                                .thumbnail(itemUsageDto.getThumbnail())
                                .totalNum(itemUsageDto.getTotalNum())
                                .build()),
                null
        );
    }

    public static UsageEntity toNewEntity(UsageDto dto, UserEntity user, ItemEntity item) {
        return UsageEntity.builder()
                .totalCode(dto.item.totalCode())
                .usageNum(dto.usageNum)
                .usageTime(LocalDateTime.now())
                .user(user)
                .item(item)
                .build();
    }



}
