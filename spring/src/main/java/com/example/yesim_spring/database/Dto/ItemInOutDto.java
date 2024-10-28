package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.repository.ItemRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import com.example.yesim_spring.util.ImageURL;

import java.time.LocalDateTime;

public record ItemInOutDto(
        long itemId,
        String itemName,
        String companyName,
        String thumbnail,
        String containerSection,
        int totalNum,
        int totalReqNum,
        int totalUsageNum
) {

    public static ItemInOutDto of(ItemRepository.interItemInOutDto interItemInOutDto){
        return new ItemInOutDto(
                interItemInOutDto.getItemId(),
                interItemInOutDto.getItemName(),
                interItemInOutDto.getCompanyName(),
                ImageURL.thumbnail(interItemInOutDto.getThumbnail()),
                interItemInOutDto.getContainerSection(),
                interItemInOutDto.getTotalNum(),
                interItemInOutDto.getTotalReqNum(),
                interItemInOutDto.getTotalUsageNum()
        );
    }

}
