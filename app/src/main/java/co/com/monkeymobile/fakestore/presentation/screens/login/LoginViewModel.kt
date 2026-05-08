package co.com.monkeymobile.fakestore.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.usecase.ValidateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateUserUseCase: ValidateUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> updateUsername(intent.username)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.Login -> login()
        }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username, error = null) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password, error = null) }
    }

    private fun login() {
        val currentState = _state.value

        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(error = "Please enter username and password") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            validateUserUseCase(currentState.username, currentState.password)
                .onSuccess { user ->
                    sessionManager.setUserId(user.id)
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(LoginEffect.NavigateToHome)
                }
                .onFailure { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                    _effect.emit(LoginEffect.ShowError(exception.message ?: "Login failed"))
                }
        }
    }
}