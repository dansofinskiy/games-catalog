package com.example.repository

import com.example.model.Game
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.annotation.EntityGraph
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface GameRepository : JpaRepository<Game, Int> {
    fun findOrderByPriorityAsc() : List<Game>

    fun findAllByIdIn(ids: List<Int>): List<Game>
}