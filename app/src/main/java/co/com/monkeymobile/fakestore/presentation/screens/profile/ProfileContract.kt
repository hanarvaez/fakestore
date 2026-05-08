package co.com.monkeymobile.fakestore.presentation.screens.profile

import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.presentation.screens.ViewEvent
import co.com.monkeymobile.fakestore.presentation.screens.ViewState

sealed class ProfileViewState : ViewState {

    data object Initial : ProfileViewState() {
        override val name: String = "ProfileViewState.Initial"
    }

    data object Loading : ProfileViewState() {
        override val name: String = "ProfileViewState.Loading"
    }

    data class Content(
        val user: User,
        val favoritesCount: Int
    ) : ProfileViewState() {
        override val name: String = "ProfileViewState.Content"
    }

    data class Error(val message: String) : ProfileViewState() {
        override val name: String = "ProfileViewState.Error"
    }
}

sealed class ProfileViewEvent : ViewEvent {

    data object LoadProfile : ProfileViewEvent() {
        override val name: String = "ProfileViewEvent.LoadProfile"
    }
}
