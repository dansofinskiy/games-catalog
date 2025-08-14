package com.example.service

import com.example.controller.dto.GameManageDto
import com.example.controller.dto.GameResponseDto
import com.example.controller.dto.GameUpdateDto

interface GameService {
    fun getGames(): List<GameManageDto>

    fun getGame(id: Int): GameManageDto?

    fun createGame(game: GameManageDto): GameManageDto

    fun updateGame(game: GameUpdateDto): GameManageDto

    fun deleteGame(id: Int)

    fun searchGames(categories: List<String>?, titleLike: String? = null): List<GameResponseDto>
}