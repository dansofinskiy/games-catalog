package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Serdeable
data class GameUpdateDto(
    @Schema(name = "Game ID", example = "1", required = true)
    var id: Int,
    @Schema(name = "Game Title", example = "Quake II: The Reckoning", required = true)
    var title: String,
    @Schema(name = "Game Priority", example = "1", required = false, defaultValue = "0")
    var priority: Int = 0,
    @Schema(name = "Game Categories", required = false, example = "[1, 2, 3]")
    var categories: List<Int> = emptyList()
)
