package co.com.monkeymobile.fakestore.presentation.screens.profile

import co.com.monkeymobile.fakestore.domain.model.User

data class ProfileState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val favoritesCount: Int = 0,
    val error: String? = null
)

sealed class ProfileIntent {
    data object LoadProfile : ProfileIntent()
}

sealed class ProfileEffect {
    data class ShowError(val message: String) : ProfileEffect()
}