package com.example.mapper

import com.example.controller.dto.GameManageDto
import com.example.controller.dto.GameResponseDto
import com.example.model.Category
import com.example.model.Game

fun Game.toManageDto(): GameManageDto {
    return GameManageDto(
        id = this.id,
        title = this.title,
        name = this.name,
        priority = this.priority,
        categories = this.categories.map { it.id!! }.toMutableList()
    )
}

fun Game.toResponseDto(): GameResponseDto {
    return GameResponseDto(
        id = this.id!!,
        title = this.title,
        name = this.name,
        priority = this.priority,
        categories = this.categories.map { it.title }.toList()
    )
}

fun GameManageDto.toNewEntity(categories: MutableList<Category>): Game {
    return Game(
        title = this.title,
        name = this.name,
        priority = this.priority,
        categories = categories
    )
}