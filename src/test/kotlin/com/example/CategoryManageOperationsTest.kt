package com.example

import com.example.controller.dto.CategoryManageDto
import com.example.controller.dto.GameManageDto
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@Sql(scripts = ["classpath:sql/game-data.sql"], phase = Sql.Phase.BEFORE_ALL)
class CategoryManageOperationsTest {
    lateinit var blockingClient: BlockingHttpClient

    @Inject
    @Client("/manage/")
    lateinit var client: HttpClient

    @BeforeEach
    fun setup() {
        blockingClient = client.toBlocking()
    }

    @Test
    fun testGetAllGames() {
        val request = HttpRequest.GET<List<GameManageDto>>("games")
        val response = blockingClient.retrieve(request, Argument.of(List::class.java, GameManageDto::class.java))
        assertEquals(4, response.size)
        assertEquals(1, (response[0] as GameManageDto).id)
        assertEquals("Doom", (response[0] as GameManageDto).name)
        assertEquals("Doom I: Shareware", (response[0] as GameManageDto).title)
        assertEquals(1, (response[0] as GameManageDto).priority)
    }

    @Test
    fun testCategoryCrud() {
        var request = HttpRequest.POST("categories", CategoryManageDto(
            title = "Test Category",
            priority = 1,
            games = listOf(1)
        ))
        var entityResponse = blockingClient.exchange(request, CategoryManageDto::class.java)

        assertEquals(HttpStatus.CREATED, entityResponse.status)
        assertNotNull(entityResponse.body())
        assertNotNull(entityResponse.body()?.id)
        assertEquals("Test Category", entityResponse.body()?.title)
        assertEquals(1, entityResponse.body()?.priority)
        assertEquals(listOf(1), entityResponse.body()?.games)

        val createdCategoryId = entityResponse.body()?.id!!

        request = HttpRequest.GET("categories/$createdCategoryId")
        val getResponse = blockingClient.retrieve(request, CategoryManageDto::class.java)
        assertEquals(createdCategoryId, getResponse.id)

        request = HttpRequest.PUT("categories", getResponse.copy(title = "updated-category"))
        val updateResponse = blockingClient.exchange(request, CategoryManageDto::class.java)
        assertEquals(HttpStatus.OK, updateResponse.status)
        assertEquals("updated-category", updateResponse.body()?.title)

        request = HttpRequest.DELETE("categories/$createdCategoryId")
        val deleteResponse = blockingClient.exchange(request, Void::class.java)
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
    }
}