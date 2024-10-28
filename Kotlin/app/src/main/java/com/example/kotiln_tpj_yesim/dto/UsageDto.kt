package com.example.kotiln_tpj_yesim.dto


data class UsageDto(
    val id:Long,
    val usageNum:Int,
    val usageTime:String,
    val item:ItemDto,
    val user:UserDto
)
