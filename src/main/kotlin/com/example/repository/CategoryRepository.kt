package com.example.repository

import com.example.model.Category
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.annotation.EntityGraph
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface CategoryRepository : JpaRepository<Category, Int> {
    fun findOrderByPriorityAsc() : List<Category>

    fun findAllByIdIn(ids: List<Int>): List<Category>
}