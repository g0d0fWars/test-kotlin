package com.example.items.controller

import com.example.items.dto.ExportRequest
import com.example.items.dto.ItemRequest
import com.example.items.dto.ItemResponse
import com.example.items.export.ExcelExportService
import com.example.items.service.ItemService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/items")
class ItemController(
    private val itemService: ItemService,
    private val excelExportService: ExcelExportService
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<ItemResponse>> =
        ResponseEntity.ok(itemService.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ItemResponse> =
        ResponseEntity.ok(itemService.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody request: ItemRequest): ResponseEntity<ItemResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(request))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: ItemRequest
    ): ResponseEntity<ItemResponse> =
        ResponseEntity.ok(itemService.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        itemService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/export")
    fun export(@Valid @RequestBody request: ExportRequest): ResponseEntity<ByteArray> {
        val items = itemService.findAllEntities()
        val bytes = excelExportService.export(items, request.columns)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=items.xlsx")
            .contentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ))
            .contentLength(bytes.size.toLong())
            .body(bytes)
    }
}
