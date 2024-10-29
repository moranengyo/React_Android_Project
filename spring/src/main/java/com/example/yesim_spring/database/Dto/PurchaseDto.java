package com.example.yesim_spring.database.Dto;

import com.example.yesim_spring.database.entity.PurchaseEntity;
import com.example.yesim_spring.database.entity.define.RequestStatus;

import java.time.LocalDateTime;

public record PurchaseDto(
        long id,
        String title,
        int reqNum,
        LocalDateTime reqTime,
        LocalDateTime approvedTime,
        String approvedStatus,
        String reqComment,
        String approvalComment,
        String newYn,
        UserDto user,
        ItemDto item
) {
    public static PurchaseDto of(PurchaseEntity entity){
        return new PurchaseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getReqNum(),
                entity.getReqTime(),
                entity.getApprovedTime(),
                entity.getApprovedStatus().toString(),
                entity.getReqComment(),
                entity.getApprovalComment(),
                entity.getNewYn(),
                UserDto.of(entity.getUser()),
                ItemDto.of(entity.getItem())
        );
    }

    public static PurchaseEntity toEntity(PurchaseDto dto){
        return PurchaseEntity.builder()
                .id(dto.id)
                .title(dto.title)
                .reqNum(dto.reqNum)
                .reqTime(dto.reqTime)
                .approvedTime(dto.approvedTime)
                .approvedStatus(RequestStatus.valueOf(dto.approvedStatus))
                .reqComment(dto.reqComment)
                .approvalComment(dto.approvalComment)
                .newYn(dto.newYn)
                .item(ItemDto.toEntity(dto.item))
                .user(UserDto.toEntity(dto.user))
                .build();
    }

    public static PurchaseEntity toNewEntity(PurchaseDto dto){
        return PurchaseEntity.builder()
                .title(dto.title)
                .reqNum(dto.reqNum)
                .reqTime(LocalDateTime.now())
                .approvedTime(null)
                // unconfirmed로 들어가면 안됨
                .approvedStatus(RequestStatus.WAIT)
                .reqComment(dto.reqComment)
                .approvalComment(null)
                .newYn(dto.newYn)
                .item(ItemDto.toEntity(dto.item))
                .user(UserDto.toEntity(dto.user))
                .build();
    }

    public static PurchaseEntity toApprovedEntity(PurchaseDto dto, String approvalComment){
        return PurchaseEntity.builder()
                .id(dto.id)
                .title(dto.title)
                .reqNum(dto.reqNum)
                .reqTime(dto.reqTime)
                .approvedTime(LocalDateTime.now())
                .approvedStatus(RequestStatus.APPROVE)
                .reqComment(dto.reqComment)
                .approvalComment(approvalComment)
                .newYn(dto.newYn)
                .item(ItemDto.toEntity(dto.item))
                .user(UserDto.toEntity(dto.user))
                .build();
    }


    public static PurchaseEntity toRejectedEntity(PurchaseDto dto, String approvalComment){
        return PurchaseEntity.builder()
                .id(dto.id)
                .title(dto.title)
                .reqNum(dto.reqNum)
                .reqTime(dto.reqTime)
                .approvedTime(LocalDateTime.now())
                .approvedStatus(RequestStatus.CANCEL)
                .reqComment(dto.reqComment)
                .approvalComment(approvalComment)
                .newYn(dto.newYn)
                .item(ItemDto.toEntity(dto.item))
                .user(UserDto.toEntity(dto.user))
                .build();
    }
}
