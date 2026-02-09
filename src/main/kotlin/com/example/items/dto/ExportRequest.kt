package com.example.items.dto

import jakarta.validation.constraints.NotEmpty

data class ExportRequest(
    @field:NotEmpty(message = "Columns list must not be empty")
    val columns: List<String> = emptyList()
)
