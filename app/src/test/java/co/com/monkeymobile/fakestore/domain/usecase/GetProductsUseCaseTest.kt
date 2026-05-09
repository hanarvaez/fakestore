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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductsUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk()
        useCase = GetProductsUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `invoke returns success with products for user`() = runTest {
        val userId = 1
        val products = listOf(
            Product(
                id = 1,
                title = "Test Product",
                price = 10.0,
                description = "Description",
                category = "electronics",
                image = "https://example.com/image.jpg",
                rating = Rating(4.5, 100),
                isFavorite = false
            )
        )

        coEvery { repository.getProducts(userId) } returns flowOf(products)

        val result = useCase(GetProductsUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(1, useCaseResult.products.size)
                assertEquals("Test Product", useCaseResult.products.first().title)
            }
        }
    }

    @Test
    fun `invoke returns success with empty list when no products`() = runTest {
        val userId = 1

        coEvery { repository.getProducts(userId) } returns flowOf(emptyList())

        val result = useCase(GetProductsUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertTrue(useCaseResult.products.isEmpty())
            }
        }
    }
}
