package com.example.service

import com.example.controller.dto.CategoryManageDto
import com.example.controller.dto.CategoryResponseDto

interface CategoryService {
    fun getCategories(): List<CategoryManageDto>

    fun getCategory(id: Int): CategoryManageDto?

    fun createCategory(category: CategoryManageDto): CategoryManageDto

    fun updateCategory(category: CategoryManageDto): CategoryManageDto

    fun deleteCategory(id: Int)

    fun searchCategories(titles: List<String>?, gameTitleLike: String? = null): List<CategoryResponseDto>
}