package com.example.items.dto

data class ItemResponse(
    val id: Long,
    val name: String,
    val extraNumber: Int?,
    val extraText: String?
)
