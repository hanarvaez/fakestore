package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoritesCountUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetFavoritesCountUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFavoritesCountUseCase(repository)
    }

    @Test
    fun `invoke returns flow of count`() = runBlocking {
        coEvery { repository.getFavoritesCount() } returns flowOf(5)

        val result = useCase()

        result.collect { count ->
            assertEquals(5, count)
        }
    }

    @Test
    fun `invoke returns zero when no favorites`() = runBlocking {
        coEvery { repository.getFavoritesCount() } returns flowOf(0)

        val result = useCase()

        result.collect { count ->
            assertEquals(0, count)
        }
    }
}