package com.example.service

import com.example.controller.dto.GameManageDto
import com.example.controller.dto.GameResponseDto
import com.example.controller.dto.GameUpdateDto
import com.example.mapper.GameGroupingKey
import com.example.mapper.GameSearchResult
import com.example.mapper.toManageDto
import com.example.mapper.toNewEntity
import com.example.repository.CategoryRepository
import com.example.repository.GameRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager

@Singleton
@Transactional
class GameServiceImpl : GameService {
    @Inject
    lateinit var gameRepository: GameRepository
    @Inject
    lateinit var categoryRepository: CategoryRepository
    @Inject
    lateinit var entityManager: EntityManager

    override fun getGames(): List<GameManageDto> = gameRepository.findOrderByPriorityAsc().map { it.toManageDto() }

    override fun getGame(id: Int): GameManageDto? {
        return gameRepository.findById(id).map { it.toManageDto() }.orElse(null)
    }

    override fun createGame(game: GameManageDto): GameManageDto {
        val categories = categoryRepository.findAllByIdIn(game.categories)
        val newGame = game.toNewEntity(categories.toMutableList())
        categories.forEach { it.games.add(newGame) }
        return gameRepository.save(newGame).toManageDto()
    }

    override fun updateGame(game: GameUpdateDto): GameManageDto {
        val categories = categoryRepository.findAllByIdIn(game.categories)
        val existingGame = gameRepository.findById(game.id)
            .orElseThrow { HttpStatusException(HttpStatus.NOT_FOUND, "Game with id ${game.id} not found") }
        existingGame.apply {
            title = game.title
            priority = game.priority

            val toDeleteLinks = this.categories - categories
            this.categories = categories.toMutableList()

            categories.forEach { it.games.add(this) }
            toDeleteLinks.forEach { it.games.remove(this) }
        }
        return gameRepository.update(existingGame).toManageDto()
    }

    override fun deleteGame(id: Int) {
       gameRepository.findById(id).ifPresentOrElse(
           { gameRepository.deleteById(id) },
           { throw HttpStatusException(HttpStatus.NOT_FOUND, "Game with id $id not found") })
    }

    override fun searchGames(categories: List<String>?, titleLike: String?): List<GameResponseDto> {
        var query = "select g.id, g.name, g.title, g.priority, c.title from game g " +
                "left join category_game cg on g.id = cg.game_id " +
                "left join category c on cg.category_id = c.id where 1=1"
        val params = mutableMapOf<String, Any>()
        categories?.let {
            query += " and c.title in :categories"
            params["categories"] = it
        }
        titleLike?.let {
            query += " and lower(g.title) like :titleLike"
            params["titleLike"] = "%${it.lowercase()}%"
        }
        query += " order by g.priority asc, c.priority asc"

        return entityManager.createNativeQuery(query, GameSearchResult::class.java)
            .apply {
                params.forEach { (key, value) -> setParameter(key, value) }
            }.resultList
            .groupBy { GameGroupingKey((it as GameSearchResult).id, it.name, it.title, it.priority) }
            .map { (game, results) ->
                GameResponseDto(
                    id = game.id,
                    name = game.name,
                    title = game.title,
                    priority = game.priority,
                    categories = results.mapNotNull { (it as GameSearchResult).categoryTitle }
                )
            }
    }
}