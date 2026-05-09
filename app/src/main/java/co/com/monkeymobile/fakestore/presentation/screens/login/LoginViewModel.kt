package co.com.monkeymobile.fakestore.presentation.screens.login

import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCase
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.ValidateCredentialsUseCaseResult
import co.com.monkeymobile.fakestore.presentation.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateCredentialsUseCase: ValidateCredentialsUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel<LoginViewState, LoginViewEvent>(
    initialState = LoginViewState.Initial
) {

    override fun dispatchViewEvent(event: LoginViewEvent) {
        super.dispatchViewEvent(event)

        viewModelScope.launch {
            when (event) {
                is LoginViewEvent.OnLoginPressed -> login(event.username, event.password)
            }
        }
    }

    private suspend fun login(username: String, password: String) {
        val cleanUsername = username.trim()
        val cleanPassword = password.trim()

        if (cleanUsername.isBlank() || cleanPassword.isBlank()) {
            showMessage("Username or password can't be blank")
            return
        }

        updateUIState(LoginViewState.Loading)

        validateCredentialsUseCase(
                ValidateCredentialsUseCaseParams(
                    username = cleanUsername,
                    password = cleanPassword
                )
            )
            .onSuccess { result ->
                val user = result.user
                sessionManager.setUserId(user.id)
                updateUIState(LoginViewState.Content(user))
            }
            .onFailure { exception ->
                updateUIState(LoginViewState.Initial)
                showMessage(exception.message ?: "Login failed")
            }
    }
}
