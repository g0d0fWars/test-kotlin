package com.example.items.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") }
        val body = mapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Validation failed",
            "fieldErrors" to errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleNotFound(ex: ItemNotFoundException): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to (ex.message ?: "Not found")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(InvalidColumnException::class)
    fun handleInvalidColumn(ex: InvalidColumnException): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to (ex.message ?: "Invalid columns")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error" to "Internal server error"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}
