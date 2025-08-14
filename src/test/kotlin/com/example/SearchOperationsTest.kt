package com.example

import com.example.controller.dto.CategoryResponseDto
import com.example.controller.dto.GameResponseDto
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@Sql(scripts = ["classpath:sql/game-data.sql", "classpath:sql/category-data.sql", "classpath:sql/links-data.sql"], phase = Sql.Phase.BEFORE_ALL)
class SearchOperationsTest {
    lateinit var blockingClient: BlockingHttpClient

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @BeforeEach
    fun setup() {
        blockingClient = client.toBlocking()
    }

    @Test
    fun testGetAllCategories() {
        var request = HttpRequest.GET<List<CategoryResponseDto>>("/categories/games/list")
        val responseAll =
            blockingClient.retrieve(request, Argument.of(List::class.java, CategoryResponseDto::class.java))
        assertEquals(3, responseAll.size)

        assertEquals(1, (responseAll[0] as CategoryResponseDto).id)
        assertEquals(1, (responseAll[0] as CategoryResponseDto).priority)
        assertEquals("Action", (responseAll[0] as CategoryResponseDto).title)

        assertEquals(2, (responseAll[1] as CategoryResponseDto).id)
        assertEquals(2, (responseAll[1] as CategoryResponseDto).priority)
        assertEquals("RPG", (responseAll[1] as CategoryResponseDto).title)

        assertEquals(3, (responseAll[2] as CategoryResponseDto).id)
        assertEquals(3, (responseAll[2] as CategoryResponseDto).priority)
        assertEquals("RTS", (responseAll[2] as CategoryResponseDto).title)

        assertEquals(4, (responseAll[0] as CategoryResponseDto).games.size)
        assertEquals(1, (responseAll[0] as CategoryResponseDto).games[0].id)
        assertEquals("Doom", (responseAll[0] as CategoryResponseDto).games[0].name)
        assertEquals("Doom I: Shareware", (responseAll[0] as CategoryResponseDto).games[0].title)
        assertEquals(1, (responseAll[0] as CategoryResponseDto).games[0].priority)

        assertEquals(2, (responseAll[0] as CategoryResponseDto).games[1].id)
        assertEquals("Quake", (responseAll[0] as CategoryResponseDto).games[1].name)
        assertEquals("Quake II: The Reckoning", (responseAll[0] as CategoryResponseDto).games[1].title)
        assertEquals(2, (responseAll[0] as CategoryResponseDto).games[1].priority)

        assertEquals(3, (responseAll[0] as CategoryResponseDto).games[2].id)
        assertEquals("Half-Life", (responseAll[0] as CategoryResponseDto).games[2].name)
        assertEquals("Half-Life 3: Episode One", (responseAll[0] as CategoryResponseDto).games[2].title)
        assertEquals(3, (responseAll[0] as CategoryResponseDto).games[2].priority)

        assertEquals(4, (responseAll[0] as CategoryResponseDto).games[3].id)
        assertEquals("Counter-Strike", (responseAll[0] as CategoryResponseDto).games[3].name)
        assertEquals("Counter-Strike: Global Offensive", (responseAll[0] as CategoryResponseDto).games[3].title)
        assertEquals(4, (responseAll[0] as CategoryResponseDto).games[3].priority)
    }

    @Test
    fun testSearchCategoriesByTitle() {
        val request = HttpRequest.GET<List<CategoryResponseDto>>("/categories/games/list?titles=RPG&titles=RTS")
        val responseFilteredByTitle =
            blockingClient.retrieve(request, Argument.of(List::class.java, CategoryResponseDto::class.java))

        assertEquals(2, responseFilteredByTitle.size)

        assertEquals(2, (responseFilteredByTitle[0] as CategoryResponseDto).id)
        assertEquals(2, (responseFilteredByTitle[0] as CategoryResponseDto).priority)
        assertEquals("RPG", (responseFilteredByTitle[0] as CategoryResponseDto).title)

        assertEquals(3, (responseFilteredByTitle[1] as CategoryResponseDto).id)
        assertEquals(3, (responseFilteredByTitle[1] as CategoryResponseDto).priority)
        assertEquals("RTS", (responseFilteredByTitle[1] as CategoryResponseDto).title)

        assertEquals(2, (responseFilteredByTitle[0] as CategoryResponseDto).games.size)

        assertEquals(2, (responseFilteredByTitle[0] as CategoryResponseDto).games[0].id)
        assertEquals("Quake", (responseFilteredByTitle[0] as CategoryResponseDto).games[0].name)
        assertEquals(2, (responseFilteredByTitle[0] as CategoryResponseDto).games[0].priority)

        assertEquals(3, (responseFilteredByTitle[0] as CategoryResponseDto).games[1].id)
        assertEquals("Half-Life", (responseFilteredByTitle[0] as CategoryResponseDto).games[1].name)
        assertEquals(3, (responseFilteredByTitle[0] as CategoryResponseDto).games[1].priority)
    }

    @Test
    fun testSearchCategoriesByGameTitle() {
        val request = HttpRequest.GET<List<CategoryResponseDto>>("/categories/games/list?gameTitleLike=Quake")
        val responseFilteredByGameTitle =
            blockingClient.retrieve(request, Argument.of(List::class.java, CategoryResponseDto::class.java))

        assertEquals(2, responseFilteredByGameTitle.size)

        assertEquals(1, (responseFilteredByGameTitle[0] as CategoryResponseDto).id)
        assertEquals(1, (responseFilteredByGameTitle[0] as CategoryResponseDto).priority)
        assertEquals("Action", (responseFilteredByGameTitle[0] as CategoryResponseDto).title)

        assertEquals(2, (responseFilteredByGameTitle[1] as CategoryResponseDto).id)
        assertEquals(2, (responseFilteredByGameTitle[1] as CategoryResponseDto).priority)
        assertEquals("RPG", (responseFilteredByGameTitle[1] as CategoryResponseDto).title)

        assertEquals(1, (responseFilteredByGameTitle[0] as CategoryResponseDto).games.size)

        assertEquals(2, (responseFilteredByGameTitle[0] as CategoryResponseDto).games[0].id)
        assertEquals("Quake", (responseFilteredByGameTitle[0] as CategoryResponseDto).games[0].name)
        assertEquals("Quake II: The Reckoning", (responseFilteredByGameTitle[0] as CategoryResponseDto).games[0].title)
        assertEquals(2, (responseFilteredByGameTitle[0] as CategoryResponseDto).games[0].priority)
    }

    @Test
    fun testSearchCategoriesByTitleAndGameTitle() {
        val request =
            HttpRequest.GET<List<CategoryResponseDto>>("/categories/games/list?titles=RPG&gameTitleLike=Half-Life")
        val responseFilteredByTitleAndGameTitle =
            blockingClient.retrieve(request, Argument.of(List::class.java, CategoryResponseDto::class.java))

        assertEquals(1, responseFilteredByTitleAndGameTitle.size)

        assertEquals(2, (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).id)
        assertEquals(2, (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).priority)
        assertEquals("RPG", (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).title)

        assertEquals(1, (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).games.size)

        assertEquals(3, (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).games[0].id)
        assertEquals("Half-Life", (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).games[0].name)
        assertEquals(
            "Half-Life 3: Episode One",
            (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).games[0].title
        )
        assertEquals(3, (responseFilteredByTitleAndGameTitle[0] as CategoryResponseDto).games[0].priority)
    }

    @Test
    fun testGetAllGames() {
        val request = HttpRequest.GET<List<GameResponseDto>>("/games/list")
        val responseAll = blockingClient.retrieve(request, Argument.of(List::class.java, GameResponseDto::class.java))
        assertEquals(4, responseAll.size)

        assertEquals(1, (responseAll[0] as GameResponseDto).id)
        assertEquals("Doom", (responseAll[0] as GameResponseDto).name)
        assertEquals("Doom I: Shareware", (responseAll[0] as GameResponseDto).title)
        assertEquals(1, (responseAll[0] as GameResponseDto).priority)

        assertEquals(2, (responseAll[1] as GameResponseDto).id)
        assertEquals("Quake", (responseAll[1] as GameResponseDto).name)
        assertEquals("Quake II: The Reckoning", (responseAll[1] as GameResponseDto).title)
        assertEquals(2, (responseAll[1] as GameResponseDto).priority)

        assertEquals(3, (responseAll[2] as GameResponseDto).id)
        assertEquals("Half-Life", (responseAll[2] as GameResponseDto).name)
        assertEquals("Half-Life 3: Episode One", (responseAll[2] as GameResponseDto).title)
        assertEquals(3, (responseAll[2] as GameResponseDto).priority)

        assertEquals(4, (responseAll[3] as GameResponseDto).id)
        assertEquals("Counter-Strike", (responseAll[3] as GameResponseDto).name)
        assertEquals("Counter-Strike: Global Offensive", (responseAll[3] as GameResponseDto).title)
        assertEquals(4, (responseAll[3] as GameResponseDto).priority)

        assertEquals(1, (responseAll[0] as GameResponseDto).categories.size)
        assertEquals("Action", (responseAll[0] as GameResponseDto).categories[0])

        assertEquals(2, (responseAll[1] as GameResponseDto).categories.size)
        assertEquals("Action", (responseAll[1] as GameResponseDto).categories[0])
        assertEquals("RPG", (responseAll[1] as GameResponseDto).categories[1])

        assertEquals(3, (responseAll[2] as GameResponseDto).categories.size)
        assertEquals("Action", (responseAll[2] as GameResponseDto).categories[0])
        assertEquals("RPG", (responseAll[2] as GameResponseDto).categories[1])
        assertEquals("RTS", (responseAll[2] as GameResponseDto).categories[2])
    }

    @Test
    fun testSearchGamesByTitle() {
        val request = HttpRequest.GET<List<GameResponseDto>>("/games/list?titleLike=Quake")
        val responseFilteredByTitle =
            blockingClient.retrieve(request, Argument.of(List::class.java, GameResponseDto::class.java))

        assertEquals(1, responseFilteredByTitle.size)

        assertEquals(2, (responseFilteredByTitle[0] as GameResponseDto).id)
        assertEquals("Quake", (responseFilteredByTitle[0] as GameResponseDto).name)
        assertEquals("Quake II: The Reckoning", (responseFilteredByTitle[0] as GameResponseDto).title)
        assertEquals(2, (responseFilteredByTitle[0] as GameResponseDto).priority)
    }

    @Test
    fun testSearchGamesByCategory() {
        val request = HttpRequest.GET<List<GameResponseDto>>("/games/list?categories=RTS&categories=RPG")
        val responseFilteredByCategory =
            blockingClient.retrieve(request, Argument.of(List::class.java, GameResponseDto::class.java))

        assertEquals(2, responseFilteredByCategory.size)

        assertEquals(2, (responseFilteredByCategory[0] as GameResponseDto).id)
        assertEquals("Quake", (responseFilteredByCategory[0] as GameResponseDto).name)
        assertEquals("Quake II: The Reckoning", (responseFilteredByCategory[0] as GameResponseDto).title)
        assertEquals(2, (responseFilteredByCategory[0] as GameResponseDto).priority)

        assertEquals(3, (responseFilteredByCategory[1] as GameResponseDto).id)
        assertEquals("Half-Life", (responseFilteredByCategory[1] as GameResponseDto).name)
        assertEquals("Half-Life 3: Episode One", (responseFilteredByCategory[1] as GameResponseDto).title)
        assertEquals(3, (responseFilteredByCategory[1] as GameResponseDto).priority)
    }

    @Test
    fun testSearchGamesByTitleAndCategory() {
        val request = HttpRequest.GET<List<GameResponseDto>>("/games/list?categories=RPG&titleLike=Half-Life")
        val responseFilteredByTitleAndCategory =
            blockingClient.retrieve(request, Argument.of(List::class.java, GameResponseDto::class.java))

        assertEquals(1, responseFilteredByTitleAndCategory.size)

        assertEquals(3, (responseFilteredByTitleAndCategory[0] as GameResponseDto).id)
        assertEquals("Half-Life", (responseFilteredByTitleAndCategory[0] as GameResponseDto).name)
        assertEquals("Half-Life 3: Episode One", (responseFilteredByTitleAndCategory[0] as GameResponseDto).title)
        assertEquals(3, (responseFilteredByTitleAndCategory[0] as GameResponseDto).priority)
    }
}