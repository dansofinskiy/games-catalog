package com.example.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "game")
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    var name: String,
    var title: String,
    var priority: Int = 0,
    @ManyToMany(mappedBy = "games", cascade = [(CascadeType.MERGE)])
    var categories: MutableList<Category> = mutableListOf(),
)
