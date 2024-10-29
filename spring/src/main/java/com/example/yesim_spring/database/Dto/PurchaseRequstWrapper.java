package com.example.yesim_spring.database.Dto;


public class PurchaseRequstWrapper {
    private ItemDto itemDto;
    private PurchaseDto purchaseDto;

    // Getters and Setters
    public ItemDto getItemDto() {
        return itemDto;
    }

    public void setItemDto(ItemDto itemDto) {
        this.itemDto = itemDto;
    }

    public PurchaseDto getPurchaseDto() {
        return purchaseDto;
    }

    public void setPurchaseDto(PurchaseDto purchaseDto) {
        this.purchaseDto = purchaseDto;
    }

}
