package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoritesUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetFavoritesUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFavoritesUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `invoke returns flow of favorites for user`() = runTest {
        val userId = 1
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

        coEvery { repository.getFavorites(userId) } returns flowOf(products)

        val result = useCase(GetFavoritesUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(1, useCaseResult.products.size)
                assertEquals("Favorite Product", useCaseResult.products.first().title)
            }
        }
    }

    @Test
    fun `invoke returns empty when user has no favorites`() = runTest {
        val userId = 1
        coEvery { repository.getFavorites(userId) } returns flowOf(emptyList())

        val result = useCase(GetFavoritesUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(0, useCaseResult.products.size)
            }
        }
    }
}
