package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetFavoritesUseCaseTest {

    private lateinit var repository: ProductRepository

    private lateinit var useCase: GetFavoritesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFavoritesUseCase(repository)
    }

    @Test
    fun `invoke returns flow of favorites`() = runBlocking {
        val products = listOf(
            Product(
                id = 1,
                title = "Favorite Product",
                price = 10.0,
                description = "Description",
                category = "electronics",
                image = "https://example.com/image.jpg",
                rating = Rating(4.5, 100),
                isFavorite = true
            )
        )
        every { repository.getFavorites() } returns flowOf(products)

        val result = useCase()

        result.onEach { collected ->
            assertEquals(1, collected.size)
            assertEquals("Favorite Product", collected.first().title)
        }.collect {}
    }

    @Test
    fun `invoke returns empty when no favorites`() = runBlocking {
        val emptyList = emptyList<Product>()
        every { repository.getFavorites() } returns flowOf(emptyList)

        val result = useCase()

        result.onEach { collected ->
            assertTrue(collected.isEmpty())
        }.collect {}
    }
}