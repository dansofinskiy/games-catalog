package com.example.controller

import com.example.controller.dto.GameManageDto
import com.example.controller.dto.GameUpdateDto
import com.example.repository.GameRepository
import com.example.service.GameService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Put
import io.micronaut.http.exceptions.HttpStatusException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Controller("/manage/games")
@Tag(name = "Game Management API")
class GameController {
    @Inject
    lateinit var gameService: GameService

    @Operation(summary = "Show all games", description = "Retrieves a list of all games with categories ids.")
    @ApiResponse(responseCode = "200", description = "Found all games")
    @Produces(MediaType.APPLICATION_JSON)
    @Get()
    fun getAllGames(): List<GameManageDto> = gameService.getGames()

    @Operation(summary = "Get game by ID", description = "Retrieves a game by its ID with categories ids.")
    @ApiResponse(responseCode = "200", description = "Found game by ID")
    @ApiResponse(responseCode = "404", description = "Game not found")
    @Produces(MediaType.APPLICATION_JSON)
    @Get("/{id}")
    fun getGameById(id: Int): GameManageDto = gameService.getGame(id)
        ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "Game with id $id not found")

    @Operation(summary = "Create a new game", description = "Creates a new game with the provided categories ids.")
    @ApiResponse(responseCode = "201", description = "Game created successfully")
    @Produces(MediaType.APPLICATION_JSON)
    @Post
    fun createGame(@Body gameDto: GameManageDto): HttpResponse<GameManageDto> {
        return HttpResponse.created(gameService.createGame(gameDto))
    }

    @Operation(summary = "Update an existing game", description = "Updates an existing game with the provided categories ids.")
    @ApiResponse(responseCode = "200", description = "Game updated successfully")
    @ApiResponse(responseCode = "404", description = "Game not found")
    @Produces(MediaType.APPLICATION_JSON)
    @Put
    fun updateGame(@Body gameDto: GameUpdateDto): HttpResponse<GameManageDto> {
        return HttpResponse.ok(gameService.updateGame(gameDto))
    }

    @Operation(summary = "Delete a game", description = "Deletes a game by its ID.")
    @ApiResponse(responseCode = "204", description = "Game deleted successfully")
    @ApiResponse(responseCode = "404", description = "Game not found")
    @Produces(MediaType.APPLICATION_JSON)
    @Delete("/{id}")
    fun deleteGame(id: Int): HttpResponse<String> {
        gameService.deleteGame(id)
        return HttpResponse.noContent()
    }
}