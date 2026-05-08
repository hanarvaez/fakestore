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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateUserUseCase: ValidateUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow<LoginViewState>(LoginViewState.Initial)
    val state: StateFlow<LoginViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun handleEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.OnLoginPressed -> login(event.username, event.password)
            is LoginViewEvent.OnNavigateToHome -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.NavigateToHome)
                }
            }
        }
    }

    private fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            return
        }

        viewModelScope.launch {
            _state.value = LoginViewState.Loading

            validateUserUseCase(username, password)
                .onSuccess { user ->
                    sessionManager.setUserId(user.id)
                    _state.value = LoginViewState.Content(user)
                    _effect.emit(LoginEffect.NavigateToHome)
                }
                .onFailure { exception ->
                    _effect.emit(LoginEffect.ShowError(exception.message ?: "Login failed"))
                }
        }
    }
}
