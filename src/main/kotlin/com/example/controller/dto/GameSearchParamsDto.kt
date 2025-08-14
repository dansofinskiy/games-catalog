package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Serdeable
data class GameSearchParamsDto(
    @Schema(description = "Categories", example = "Action")
    var categories: List<String>? = null,
    @Schema(description = "Game Title Like", example = "Quake")
    var titleLike: String? = null
)
