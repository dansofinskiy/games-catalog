package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Serdeable
data class CategorySearchParamsDto(
    @Schema(description = "Game Title Like", example = "Quake")
    var gameTitleLike: String? = null,
    @Schema(description = "Titles", example = "Action")
    var titles: List<String>? = null
)
