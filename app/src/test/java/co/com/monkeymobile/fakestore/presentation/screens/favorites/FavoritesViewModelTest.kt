package co.com.monkeymobile.fakestore.presentation.screens.favorites

import android.util.Log
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesUseCaseResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private lateinit var getFavoritesUseCase: GetFavoritesUseCase
    private lateinit var toggleFavoriteUseCase: co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        getFavoritesUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        sessionManager = mockk()
        viewModel = FavoritesViewModel(
            getFavoritesUseCase = getFavoritesUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            sessionManager = sessionManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `LoadFavorites updates state to Content when favorites exist`() = runTest {
        val userId = 1
        val products = listOf(
            Product(1, "Favorite 1", 10.0, "Desc", "cat", "url", Rating(4.0, 10), true)
        )

        every { sessionManager.getUserId() } returns userId
        coEvery { getFavoritesUseCase(GetFavoritesUseCaseParams(userId)) } returns flowOf(
            Result.success(GetFavoritesUseCaseResult(products))
        )

        viewModel.dispatchViewEvent(FavoritesViewEvent.LoadFavorites)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is FavoritesViewState.Content)
        assertEquals(1, (state as FavoritesViewState.Content).products.size)
    }

    @Test
    fun `LoadFavorites updates state to Empty when no favorites`() = runTest {
        val userId = 1

        every { sessionManager.getUserId() } returns userId
        coEvery { getFavoritesUseCase(GetFavoritesUseCaseParams(userId)) } returns flowOf(
            Result.success(GetFavoritesUseCaseResult(emptyList()))
        )

        viewModel.dispatchViewEvent(FavoritesViewEvent.LoadFavorites)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is FavoritesViewState.Empty)
    }

    @Test
    fun `LoadFavorites updates state to Empty on failure`() = runTest {
        val userId = 1

        every { sessionManager.getUserId() } returns userId
        coEvery { getFavoritesUseCase(GetFavoritesUseCaseParams(userId)) } returns flowOf(
            Result.failure(Exception("Failed to load"))
        )

        viewModel.dispatchViewEvent(FavoritesViewEvent.LoadFavorites)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is FavoritesViewState.Empty)
    }
}
