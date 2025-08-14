package com.example.controller

import com.example.controller.dto.CategoryResponseDto
import com.example.controller.dto.CategorySearchParamsDto
import com.example.controller.dto.GameResponseDto
import com.example.controller.dto.GameSearchParamsDto
import com.example.service.CategoryService
import com.example.service.GameService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Controller
@Tag(name = "Search API")
class SearchController {
    @Inject
    lateinit var gameService: GameService
    @Inject
    lateinit var categoryService: CategoryService

    @Operation(summary = "Search games", description = "Searches for games based on categories and title like.")
    @ApiResponse(responseCode = "200", description = "Found games by given arguments")
    @Produces(MediaType.APPLICATION_JSON)
    @Get("/games/list{?args*}")
    fun searchGames(args: GameSearchParamsDto): List<GameResponseDto> {
        return gameService.searchGames(args.categories, args.titleLike)
    }

    @Operation(summary = "Search categories", description = "Searches for categories based on titles and game title like.")
    @ApiResponse(responseCode = "200", description = "Found categories by given arguments")
    @Produces(MediaType.APPLICATION_JSON)
    @Get("/categories/games/list{?args*}")
    fun searchCategories(args: CategorySearchParamsDto): List<CategoryResponseDto> {
        return categoryService.searchCategories(args.titles, args.gameTitleLike)
    }
}