package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.ItemEntity;
import com.example.yesim_spring.util.ImageURL;

public record ItemDto(
        long id,
        String totalCode,
        String name,
        int minNum,
        int totalNum,
        int price,
        String thumbNail,
        ContainerDto container,
        CompanyDto company
) {
    public static ItemDto of(ItemEntity entity){
        return new ItemDto(
                entity.getId(),
                entity.getTotalCode(),
                entity.getName(),
                entity.getMinNum(),
                entity.getTotalNum(),
                entity.getPrice(),
                ImageURL.thumbnail(entity.getThumbnail()),
                ContainerDto.of(entity.getContainer()),
                CompanyDto.of(entity.getCompany())
        );
    }

    public static ItemEntity toEntity(ItemDto dto){
        return ItemEntity.builder()
                .id(dto.id)
                .totalCode(dto.totalCode)
                .name(dto.name)
                .minNum(dto.minNum)
                .totalNum(dto.totalNum)
                .price(dto.price)
                .thumbnail(dto.thumbNail)
                .container(ContainerDto.toEntity(dto.container))
                .company(CompanyDto.toEntity(dto.company))
                .build();
    }


    public static ItemEntity toNewItemEntity(ItemDto dto){
        return ItemEntity.builder()
                .totalCode(dto.totalCode)
                .name(dto.name)
                .minNum(dto.minNum)
                .totalNum(dto.totalNum)
                .price(dto.price)
                .thumbnail(dto.thumbNail)
                .container(ContainerDto.toEntity(dto.container))
                .company(CompanyDto.toEntity(dto.company))
                .build();
    }

}
