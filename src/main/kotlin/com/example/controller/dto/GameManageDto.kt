package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Serdeable
data class GameManageDto(
    @Schema(name = "Game ID", example = "1", required = false)
    var id: Int? = null,
    @Schema(name = "Game Name", example = "Quake", required = true)
    var name: String,
    @Schema(name = "Game Title", required = true, example = "Game Title")
    var title: String,
    @Schema(name = "Game Priority", required = false, example = "1", defaultValue = "0")
    var priority: Int = 0,
    @Schema(name = "Game Categories", required = false, example = "[1, 2, 3]")
    var categories: List<Int> = emptyList()
)

