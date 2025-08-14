package com.example.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "category", indexes = [Index(name = "idx_category_title", columnList = "title")])
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    var title: String,
    var priority: Int = 0,
    @ManyToMany(cascade = [(CascadeType.MERGE)])
    @JoinTable(name = "category_game",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "game_id")],
        foreignKey = ForeignKey(name = "fk_category_game_category"),
        inverseForeignKey = ForeignKey(name = "fk_category_game_game"))
    var games: MutableList<Game> = mutableListOf()
)
