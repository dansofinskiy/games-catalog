package com.example.controller.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema

@Serdeable
data class CategoryManageDto(
    @Schema(name = "Category ID", example = "1", required = false)
    var id: Int? = null,
    @Schema(name = "Category Name", required = true, example = "Action")
    var title: String,
    @Schema(name = "Category Priority", required = false, example = "1", defaultValue = "0")
    var priority: Int = 0,
    @Schema(name = "Games in Category", required = false, example = "[1, 2, 3]")
    var games: List<Int> = emptyList()
) {
}