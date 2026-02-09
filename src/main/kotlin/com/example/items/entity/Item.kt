package com.example.items.entity

import jakarta.persistence.*

@Entity
@Table(name = "items")
class Item(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false, length = 255)
    var name: String = "",

    @Column(name = "extra_number")
    var extraNumber: Int? = null,

    @Column(name = "extra_text", length = 500)
    var extraText: String? = null
)
