package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CategoryResponseDto(
    var id: Int,
    var title: String,
    var priority: Int,
    var games: List<GameResponseDto> = emptyList()
)
