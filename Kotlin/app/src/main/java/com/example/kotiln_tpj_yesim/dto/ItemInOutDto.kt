package com.example.kotiln_tpj_yesim.dto

data class ItemInOutDto(
    val itemId:Double,
    val itemName:String,
    val companyName:String,
    val thumbnail:String,
    val containerSection:String,
    val totalNum:Int,
    val totalReqNum:Int,
    val totalUsageNum:Int
)