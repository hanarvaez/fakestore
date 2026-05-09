package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoritesCountUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetFavoritesCountUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFavoritesCountUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `invoke returns flow with count from repository`() = runTest {
        val userId = 1

        coEvery { repository.getFavoritesCount(userId) } returns flowOf(5)

        val result = useCase(GetFavoritesCountUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(5, useCaseResult.count)
            }
        }
    }

    @Test
    fun `invoke returns zero when no favorites`() = runTest {
        val userId = 1

        coEvery { repository.getFavoritesCount(userId) } returns flowOf(0)

        val result = useCase(GetFavoritesCountUseCaseParams(userId))

        result.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(0, useCaseResult.count)
            }
        }
    }

    @Test
    fun `invoke returns different count for different users`() = runTest {
        val userId1 = 1
        val userId2 = 2

        coEvery { repository.getFavoritesCount(userId1) } returns flowOf(3)
        coEvery { repository.getFavoritesCount(userId2) } returns flowOf(7)

        val result1 = useCase(GetFavoritesCountUseCaseParams(userId1))
        val result2 = useCase(GetFavoritesCountUseCaseParams(userId2))

        result1.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(3, useCaseResult.count)
            }
        }

        result2.collect { resultFlow ->
            resultFlow.onSuccess { useCaseResult ->
                assertEquals(7, useCaseResult.count)
            }
        }
    }
}
