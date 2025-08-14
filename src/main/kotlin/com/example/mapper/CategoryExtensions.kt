package com.example.mapper

import com.example.controller.dto.CategoryManageDto
import com.example.controller.dto.CategoryResponseDto
import com.example.controller.dto.GameResponseDto
import com.example.model.Category
import com.example.model.Game

fun Category.toManageDto(): CategoryManageDto {
    return CategoryManageDto(
        id = this.id,
        title = this.title,
        priority = this.priority,
        games = this.games.map { it.id!! }.toMutableList()
    )
}

fun Category.toResponseDto(): CategoryResponseDto {
    return CategoryResponseDto(
        id = this.id!!,
        title = this.title,
        priority = this.priority,
        games = this.games.map {
            game -> GameResponseDto(
            id = game.id!!,
            name = game.name,
            title = game.title,
            priority = game.priority
            )
        })
}

fun CategoryManageDto.toNewEntity(games: MutableList<Game>): Category {
    return Category(
        title = this.title,
        priority = this.priority,
        games = games
    )
}

data class CategorySearchResult(
    val id: Int,
    val title: String,
    val priority: Int,
    val gameId: Int?,
    val gameName: String?,
    val gameTitle: String?,
    val gamePriority: Int?
)
