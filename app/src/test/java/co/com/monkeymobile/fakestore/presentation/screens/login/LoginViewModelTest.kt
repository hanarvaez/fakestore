package co.com.monkeymobile.fakestore.presentation.screens.login

import android.util.Log
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCase
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCaseResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class LoginViewModelTest {

    private lateinit var validateCredentialsUseCase: ValidateCredentialsUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        validateCredentialsUseCase = mockk()
        sessionManager = mockk()
        viewModel = LoginViewModel(
            validateCredentialsUseCase = validateCredentialsUseCase,
            sessionManager = sessionManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `OnLoginPressed updates state to Content on success`() = runTest {
        val username = "testuser"
        val password = "password123"
        val user = User(1, "test@test.com", username, password, Name("John", "Doe"), "123", 0)

        coEvery {
            validateCredentialsUseCase(
                ValidateCredentialsUseCaseParams(
                    username,
                    password
                )
            )
        } returns Result.success(
            ValidateCredentialsUseCaseResult(user)
        )
        every { sessionManager.setUserId(user.id) } returns Unit

        viewModel.dispatchViewEvent(LoginViewEvent.OnLoginPressed(username, password))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LoginViewState.Content)
        assertEquals("testuser", (state as LoginViewState.Content).user.username)
        verify { sessionManager.setUserId(user.id) }
    }

    @Test
    fun `OnLoginPressed updates state to Initial on failure`() = runTest {
        val username = "wronguser"
        val password = "wrongpassword"

        coEvery {
            validateCredentialsUseCase(
                ValidateCredentialsUseCaseParams(
                    username,
                    password
                )
            )
        } returns Result.failure(
            Exception("Invalid username or password")
        )

        viewModel.dispatchViewEvent(LoginViewEvent.OnLoginPressed(username, password))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LoginViewState.Initial)
    }

    @Test
    fun `OnLoginPressed does not call useCase when username is blank`() = runTest {
        val username = "   "
        val password = "password123"

        viewModel.dispatchViewEvent(LoginViewEvent.OnLoginPressed(username, password))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LoginViewState.Initial)
    }

    @Test
    fun `OnLoginPressed does not call useCase when password is blank`() = runTest {
        val username = "testuser"
        val password = "   "

        viewModel.dispatchViewEvent(LoginViewEvent.OnLoginPressed(username, password))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LoginViewState.Initial)
    }
}
