package com.example.kotiln_tpj_yesim.data

data class SearchItem(
    val imageResId: Int,
    val title: String,
    val subtitle: String,
    val admin: String,
    val quantity: String,
    val iteminfo: Int // New field for additional information (e.g., stock quantity)
)