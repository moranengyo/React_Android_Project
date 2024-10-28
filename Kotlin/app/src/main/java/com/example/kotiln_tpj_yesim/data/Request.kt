package com.example.kotiln_tpj_yesim.data

data class Request(
    val date: String,
    val title: String,
    val status: String,
    val name: String,
    var isNew: Boolean,
    var count: Int,
)