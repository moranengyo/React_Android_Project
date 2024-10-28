package com.example.kotiln_tpj_yesim.dto

import java.time.LocalDateTime

data class PurchaseDto(
    val id:Long,
    val title:String,
    val reqNum:Int,
    val reqTime:String,
    val approvedTime:String,
    val approvedStatus:String,
    val reqComment:String,
    val approvalComment:String,
    val newYn:String,
    val item:ItemDto,
    val user:UserDto
)