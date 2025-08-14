package com.example.service

import com.example.controller.dto.CategoryManageDto
import com.example.controller.dto.CategoryResponseDto
import com.example.mapper.toManageDto
import com.example.mapper.toNewEntity
import com.example.mapper.toResponseDto
import com.example.model.Category
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
class CategoryServiceImpl : CategoryService {
    @Inject
    lateinit var gameRepository: GameRepository
    @Inject
    lateinit var categoryRepository: CategoryRepository
    @Inject
    lateinit var entityManager: EntityManager

    override fun getCategories(): List<CategoryManageDto> =
        categoryRepository.findOrderByPriorityAsc().map { it.toManageDto() }

    override fun getCategory(id: Int): CategoryManageDto? =
        categoryRepository.findById(id).map { it.toManageDto() }.orElse(null)

    override fun createCategory(category: CategoryManageDto): CategoryManageDto {
        val games = gameRepository.findAllByIdIn(category.games)
        val newCategory = category.toNewEntity(games.toMutableList())
        games.forEach { it.categories.add(newCategory) }
        return categoryRepository.save(newCategory).toManageDto()
    }

    override fun updateCategory(category: CategoryManageDto): CategoryManageDto {
        val games = gameRepository.findAllByIdIn(category.games)
        val existingCategory = categoryRepository.findById(category.id!!)
            .orElseThrow { HttpStatusException(HttpStatus.NOT_FOUND, "Category with id ${category.id} not found") }
        existingCategory.apply {
            title = category.title
            priority = category.priority

            val toDeleteLinks = this.games - games
            this.games = games.toMutableList()

            games.forEach { it.categories.add(this) }
            toDeleteLinks.forEach { it.categories.remove(this) }
        }
        return categoryRepository.update(existingCategory).toManageDto()
    }

    override fun deleteCategory(id: Int) {
        categoryRepository.findById(id).ifPresentOrElse(
            { categoryRepository.deleteById(id)},
            { throw HttpStatusException(HttpStatus.NOT_FOUND, "Category with id $id not found") })
    }

    override fun searchCategories(titles: List<String>?, gameTitleLike: String?): List<CategoryResponseDto> {
        var query = "select c from Category c left join c.games g where 1=1"
        val params = mutableMapOf<String, Any>()

        titles?.let {
            query += " and c.title in :titles"
            params["titles"] = it
        }
        gameTitleLike?.let {
            query += " and lower(g.title) like :gameTitleLike"
            params["gameTitleLike"] = "%${it.lowercase()}%"
        }
        query += " order by c.priority asc, g.priority asc"

        return entityManager.createQuery(query, Category::class.java)
            .apply {
                params.forEach { (key, value) -> setParameter(key, value) }
            }
            .resultList
            .map { it.toResponseDto() }
    }
}