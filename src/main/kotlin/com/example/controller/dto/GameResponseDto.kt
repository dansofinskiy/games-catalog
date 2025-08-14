package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class GameResponseDto(
    var id: Int,
    var name: String,
    var title: String,
    var priority: Int,
    var categories: List<String> = emptyList()
)
