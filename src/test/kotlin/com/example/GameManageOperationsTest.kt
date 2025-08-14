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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.assertNotNull
import org.testcontainers.junit.jupiter.Testcontainers


@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@Sql(scripts = ["classpath:sql/category-data.sql"], phase = Sql.Phase.BEFORE_ALL)
class GameManageOperationsTest {
    lateinit var blockingClient: BlockingHttpClient

    @Inject
    @Client("/manage/")
    lateinit var client: HttpClient

    @BeforeEach
    fun setup() {
        blockingClient = client.toBlocking()
    }

    @Test
    fun testGetAllCategories() {
        val request = HttpRequest.GET<List<CategoryManageDto>>("categories")
        val response = blockingClient.retrieve(request, Argument.of(List::class.java, CategoryManageDto::class.java))
        assertEquals(3, response.size)
        assertEquals(1, (response[0] as CategoryManageDto).id)
        assertEquals(1, (response[0] as CategoryManageDto).priority)
        assertEquals("Action", (response[0] as CategoryManageDto).title)
    }


    @Test
    fun testGameCrud() {
        var request = HttpRequest.POST("games", GameManageDto(
            name = "test-game",
            title = "Test Game",
            priority = 1,
            categories = listOf(1)
        ))
        var entityResponse = blockingClient.exchange(request, GameManageDto::class.java)

        assertEquals(HttpStatus.CREATED, entityResponse.status)
        assertNotNull(entityResponse.body())
        assertNotNull(entityResponse.body()?.id)
        assertEquals("test-game", entityResponse.body()?.name)
        assertEquals("Test Game", entityResponse.body()?.title)
        assertEquals(1, entityResponse.body()?.priority)
        assertEquals(listOf(1), entityResponse.body()?.categories)
        val createdGameId = entityResponse.body().id!!

        request = HttpRequest.PUT("games", GameManageDto(
            id = createdGameId,
            name = "updated-game",
            title = "Updated Game",
            priority = 2,
            categories = listOf(2)
        ))

        entityResponse = blockingClient.exchange(request, GameManageDto::class.java)
        assertEquals(HttpStatus.OK, entityResponse.status)
        assertNotNull(entityResponse.body())
        assertEquals(createdGameId, entityResponse.body()?.id)
        assertEquals("test-game", entityResponse.body()?.name)
        assertEquals("Updated Game", entityResponse.body()?.title)
        assertEquals(2, entityResponse.body()?.priority)
        assertEquals(listOf(2), entityResponse.body()?.categories)

        request = HttpRequest.GET("games/$createdGameId")
        val getResponse = blockingClient.retrieve(request, GameManageDto::class.java)
        assertNotNull(getResponse)
        assertEquals(createdGameId, getResponse.id)
        assertEquals("test-game", getResponse.name)
        assertEquals("Updated Game", getResponse.title)
        assertEquals(2, getResponse.priority)
        assertEquals(listOf(2), getResponse.categories)

        request = HttpRequest.DELETE("games/$createdGameId")
        val deleteResponse = blockingClient.exchange(request, Void::class.java)
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
    }
}