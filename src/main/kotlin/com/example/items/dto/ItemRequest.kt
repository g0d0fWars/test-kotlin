package com.example.items.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ItemRequest(
    @field:NotBlank(message = "Name is required and must not be blank")
    @field:Size(max = 255, message = "Name must not exceed 255 characters")
    val name: String = "",

    val extraNumber: Int? = null,

    @field:Size(max = 500, message = "Extra text must not exceed 500 characters")
    val extraText: String? = null
)
