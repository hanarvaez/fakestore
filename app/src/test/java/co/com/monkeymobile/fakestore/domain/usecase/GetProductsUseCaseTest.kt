package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetProductsUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns products`() = runBlocking {
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
        coEvery { repository.getProducts() } returns Result.success(products)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Test Product", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runBlocking {
        coEvery { repository.getProducts() } returns Result.failure(Exception("Network error"))

        val result = useCase()

        assertTrue(result.isFailure)
    }
}