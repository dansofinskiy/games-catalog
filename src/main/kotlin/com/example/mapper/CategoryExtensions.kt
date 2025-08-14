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
//
//fun Category.updateFromDto(dto: CategoryManageDto, games: MutableList<Game>) {
//    this.title = dto.title
//    this.priority = dto.priority
//    val toDeleteLinks = this.games - games
//    this.games = games.toMutableList()
//
//    games.forEach { it.categories.add(this) }
//    toDeleteLinks.forEach { it.categories.remove(this) }
//}
