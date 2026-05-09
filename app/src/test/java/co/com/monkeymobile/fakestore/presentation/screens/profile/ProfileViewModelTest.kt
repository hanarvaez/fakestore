package co.com.monkeymobile.fakestore.presentation.screens.profile

import android.util.Log
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesCountUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesCountUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesCountUseCaseResult
import co.com.monkeymobile.fakestore.domain.usecase.GetUserUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetUserUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.GetUserUseCaseResult
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
class ProfileViewModelTest {

    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var getFavoritesCountUseCase: GetFavoritesCountUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        getUserUseCase = mockk()
        getFavoritesCountUseCase = mockk()
        sessionManager = mockk()
        viewModel = ProfileViewModel(
            getUserUseCase = getUserUseCase,
            getFavoritesCountUseCase = getFavoritesCountUseCase,
            sessionManager = sessionManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `LoadProfile updates state to Content on success`() = runTest {
        val userId = 1
        val user = User(1, "test@test.com", "testuser", "password", Name("John", "Doe"), "123", 0)

        every { sessionManager.getUserId() } returns userId
        coEvery { getUserUseCase(GetUserUseCaseParams(userId)) } returns Result.success(
            GetUserUseCaseResult(user)
        )
        coEvery { getFavoritesCountUseCase(GetFavoritesCountUseCaseParams(userId)) } returns flowOf(
            Result.success(GetFavoritesCountUseCaseResult(5))
        )

        viewModel.dispatchViewEvent(ProfileViewEvent.LoadProfile)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ProfileViewState.Content)
        assertEquals("testuser", (state as ProfileViewState.Content).user.username)
        assertEquals(5, state.favoritesCount)
    }

    @Test
    fun `LoadProfile updates state to Error when getUser fails`() = runTest {
        val userId = 1

        every { sessionManager.getUserId() } returns userId
        coEvery { getUserUseCase(GetUserUseCaseParams(userId)) } returns Result.failure(
            Exception("User not found")
        )

        viewModel.dispatchViewEvent(ProfileViewEvent.LoadProfile)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ProfileViewState.Error)
    }
}
