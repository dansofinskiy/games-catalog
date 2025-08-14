package com.example.controller

import com.example.controller.dto.CategoryManageDto
import com.example.service.CategoryService
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

@Controller("/manage/categories")
@Tag(name = "Category Management API")
class CategoryController(
    val categoryService: CategoryService
) {

    @Operation(summary = "Show all categories", description = "Retrieves a list of all categories with games ids.")
    @ApiResponse(responseCode = "200", description = "Found all categories")
    @Produces(MediaType.APPLICATION_JSON)
    @Get()
    fun getAllCategories(): List<CategoryManageDto> = categoryService.getCategories()

    @Operation(summary = "Create category", description = "Creates a new category with the provided games ids.")
    @ApiResponse(responseCode = "201", description = "Found category by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Post()
    fun createCategory(@Body categoryDto: CategoryManageDto): HttpResponse<CategoryManageDto> {
        return HttpResponse.created(categoryService.createCategory(categoryDto))
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID with games ids.")
    @ApiResponse(responseCode = "200", description = "Found category by ID")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @Produces(MediaType.APPLICATION_JSON)
    @Get("/{id}")
    fun getCategoryById(id: Int): CategoryManageDto {
        return categoryService.getCategory(id)
            ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "Category with id $id not found")
    }

    @Operation(summary = "Update an existing category", description = "Updates an existing category with the provided games ids.")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @Produces(MediaType.APPLICATION_JSON)
    @Put()
    fun updateCategory(@Body categoryDto: CategoryManageDto): HttpResponse<CategoryManageDto> {
        return HttpResponse.ok(categoryService.updateCategory(categoryDto))
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID.")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @Delete("/{id}")
    fun deleteCategory(id: Int): HttpResponse<Void> {
        categoryService.deleteCategory(id)
        return HttpResponse.noContent()
    }
}