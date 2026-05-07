package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private lateinit var repository: ProductRepository

    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke adds favorite when product is not favorite`() = runBlocking {
        val product = createProduct(1, false)
        coEvery { repository.isFavorite(1) } returns false

        useCase(product)

        coVerify { repository.addFavorite(product) }
    }

    @Test
    fun `invoke removes favorite when product is already favorite`() = runBlocking {
        val product = createProduct(1, true)
        coEvery { repository.isFavorite(1) } returns true

        useCase(product)

        coVerify { repository.removeFavorite(1) }
    }

    private fun createProduct(id: Int, isFavorite: Boolean) = Product(
        id = id,
        title = "Test",
        price = 10.0,
        description = "Desc",
        category = "cat",
        image = "url",
        rating = Rating(4.0, 10),
        isFavorite = isFavorite
    )
}