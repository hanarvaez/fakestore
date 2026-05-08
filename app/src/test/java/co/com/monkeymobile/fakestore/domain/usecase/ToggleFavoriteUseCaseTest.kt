package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: ToggleFavoriteUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = ToggleFavoriteUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `invoke adds favorite when product is not favorite`() = runTest {
        val userId = 1
        val product = createProduct(1, false)

        coEvery { repository.isFavorite(1, userId) } returns false

        useCase(ToggleFavoriteUseCaseParams(product, userId))

        coVerify { repository.addFavorite(product, userId) }
    }

    @Test
    fun `invoke removes favorite when product is already favorite`() = runTest {
        val userId = 1
        val product = createProduct(1, true)

        coEvery { repository.isFavorite(1, userId) } returns true

        useCase(ToggleFavoriteUseCaseParams(product, userId))

        coVerify { repository.removeFavorite(1, userId) }
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
