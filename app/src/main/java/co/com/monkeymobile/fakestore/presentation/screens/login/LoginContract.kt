package co.com.monkeymobile.fakestore.presentation.screens.login

import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.presentation.screens.ViewEvent
import co.com.monkeymobile.fakestore.presentation.screens.ViewState

sealed class LoginViewState : ViewState {

    data object Initial : LoginViewState() {
        override val name: String = "LoginViewState.Initial"
    }

    data object Loading : LoginViewState() {
        override val name: String = "LoginViewState.Loading"
    }

    data class Content(val user: User) : LoginViewState() {
        override val name: String = "LoginViewState.Content"
    }
}

sealed class LoginViewEvent: ViewEvent {

    data class OnLoginPressed(
        val username: String,
        val password: String
    ) : LoginViewEvent() {
        override val name: String = "LoginViewEvent.OnLoginPressed"
    }

    data class OnNavigateToHome(val user: User) : LoginViewEvent(){
        override val name: String = "LoginViewEvent.OnNavigateToHome"
    }
}

sealed class LoginEffect {
    data object NavigateToHome : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}