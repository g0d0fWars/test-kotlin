package com.example.items.exception

class ItemNotFoundException(id: Long) : RuntimeException("Item with id $id not found")

class InvalidColumnException(columns: List<String>) :
    RuntimeException("Invalid column names: ${columns.joinToString(", ")}")
