package com.example.kotiln_tpj_yesim.dto

data class ItemDto(
    val id:Long,
    val totalCode:String,
    val name:String,
    val minNum:Int,
    val totalNum:Int,
    val price:Int,
    val thumbNail:String,
    val container:ContainerDto,
    val company:CompanyDto
)
