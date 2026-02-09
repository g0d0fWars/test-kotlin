package com.example.items.service

import com.example.items.dto.ItemRequest
import com.example.items.dto.ItemResponse
import com.example.items.entity.Item
import com.example.items.exception.ItemNotFoundException
import com.example.items.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(private val itemRepository: ItemRepository) {

    fun findAll(): List<ItemResponse> =
        itemRepository.findAll().map { it.toResponse() }

    fun findById(id: Long): ItemResponse =
        itemRepository.findById(id)
            .orElseThrow { ItemNotFoundException(id) }
            .toResponse()

    @Transactional
    fun create(request: ItemRequest): ItemResponse {
        val item = Item(
            name = request.name,
            extraNumber = request.extraNumber,
            extraText = request.extraText
        )
        return itemRepository.save(item).toResponse()
    }

    @Transactional
    fun update(id: Long, request: ItemRequest): ItemResponse {
        val item = itemRepository.findById(id)
            .orElseThrow { ItemNotFoundException(id) }
        item.name = request.name
        item.extraNumber = request.extraNumber
        item.extraText = request.extraText
        return itemRepository.save(item).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!itemRepository.existsById(id)) {
            throw ItemNotFoundException(id)
        }
        itemRepository.deleteById(id)
    }

    fun findAllEntities(): List<Item> =
        itemRepository.findAll()

    private fun Item.toResponse() = ItemResponse(
        id = this.id,
        name = this.name,
        extraNumber = this.extraNumber,
        extraText = this.extraText
    )
}
