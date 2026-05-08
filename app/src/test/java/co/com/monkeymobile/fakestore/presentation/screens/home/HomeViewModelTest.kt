package co.com.monkeymobile.fakestore.presentation.screens.home

import android.util.Log
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.usecase.GetProductsUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetProductsUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.GetProductsUseCaseResult
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
class HomeViewModelTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var toggleFavoriteUseCase: co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        getProductsUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        sessionManager = mockk()
        viewModel = HomeViewModel(
            getProductsUseCase = getProductsUseCase,
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
    fun `LoadProducts updates state to Content on success`() = runTest {
        val userId = 1
        val products = listOf(
            Product(1, "Product 1", 10.0, "Desc", "cat", "url", Rating(4.0, 10), false)
        )

        every { sessionManager.getUserId() } returns userId
        coEvery { getProductsUseCase(GetProductsUseCaseParams(userId)) } returns flowOf(
            Result.success(GetProductsUseCaseResult(products))
        )

        viewModel.dispatchViewEvent(HomeViewEvent.LoadProducts)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is HomeViewState.Content)
        assertEquals(1, (state as HomeViewState.Content).products.size)
    }

    @Test
    fun `LoadProducts updates state to Error on failure`() = runTest {
        val userId = 1

        every { sessionManager.getUserId() } returns userId
        coEvery { getProductsUseCase(GetProductsUseCaseParams(userId)) } returns flowOf(
            Result.failure(Exception("Network error"))
        )

        viewModel.dispatchViewEvent(HomeViewEvent.LoadProducts)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is HomeViewState.Error)
    }
}