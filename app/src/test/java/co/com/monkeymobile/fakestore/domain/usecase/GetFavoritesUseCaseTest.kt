package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.doReturn

@RunWith(MockitoJUnitRunner::class)
class GetFavoritesUseCaseTest {

    @Mock
    private lateinit var repository: ProductRepository

    private lateinit var useCase: GetFavoritesUseCase

    @Before
    fun setup() {
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
        doReturn(flowOf(products)).`when`(repository).getFavorites()

        val result = useCase()

        result.collect { collected ->
            assertEquals(1, collected.size)
            assertEquals("Favorite Product", collected.first().title)
        }
    }

    @Test
    fun `invoke returns empty when no favorites`() = runBlocking {
        val emptyList = emptyList<Product>()
        doReturn(flowOf(emptyList)).`when`(repository).getFavorites()

        val result = useCase()

        result.collect { collected ->
            assertTrue(collected.isEmpty())
        }
    }
}