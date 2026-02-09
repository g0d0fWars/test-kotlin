package com.example.items.export

import com.example.items.entity.Item
import com.example.items.exception.InvalidColumnException
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class ExcelExportService {

    companion object {
        val ALLOWED_COLUMNS = mapOf(
            "id" to "ID",
            "name" to "Name",
            "extraNumber" to "Extra Number",
            "extraText" to "Extra Text"
        )
    }

    fun export(items: List<Item>, columns: List<String>): ByteArray {
        validateColumns(columns)

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Items")

        // Header row
        val headerRow = sheet.createRow(0)
        columns.forEachIndexed { colIdx, colName ->
            headerRow.createCell(colIdx).setCellValue(ALLOWED_COLUMNS[colName] ?: colName)
        }

        // Data rows
        items.forEachIndexed { rowIdx, item ->
            val row = sheet.createRow(rowIdx + 1)
            columns.forEachIndexed { colIdx, colName ->
                val cell = row.createCell(colIdx)
                when (colName) {
                    "id" -> cell.setCellValue(item.id.toDouble())
                    "name" -> cell.setCellValue(item.name)
                    "extraNumber" -> item.extraNumber?.let { cell.setCellValue(it.toDouble()) }
                    "extraText" -> item.extraText?.let { cell.setCellValue(it) }
                }
            }
        }

        // Auto-size columns
        columns.indices.forEach { sheet.autoSizeColumn(it) }

        val outputStream = ByteArrayOutputStream()
        workbook.use { it.write(outputStream) }
        return outputStream.toByteArray()
    }

    private fun validateColumns(columns: List<String>) {
        val invalid = columns.filter { it !in ALLOWED_COLUMNS }
        if (invalid.isNotEmpty()) {
            throw InvalidColumnException(invalid)
        }
    }
}
