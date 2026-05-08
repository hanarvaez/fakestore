package co.com.monkeymobile.fakestore.presentation.screens.profile

import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.di.SessionManager
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesCountUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesCountUseCaseParams
import co.com.monkeymobile.fakestore.domain.usecase.GetUserUseCase
import co.com.monkeymobile.fakestore.domain.usecase.GetUserUseCaseParams
import co.com.monkeymobile.fakestore.presentation.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getFavoritesCountUseCase: GetFavoritesCountUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel<ProfileViewState, ProfileViewEvent>(
    initialState = ProfileViewState.Initial
) {

    override fun dispatchViewEvent(event: ProfileViewEvent) {
        super.dispatchViewEvent(event)

        viewModelScope.launch {
            when (event) {
                is ProfileViewEvent.LoadProfile -> loadProfile()
            }
        }
    }

    private suspend fun loadProfile() {
        updateUIState(ProfileViewState.Loading)

        val userId = sessionManager.getUserId()

        getUserUseCase(GetUserUseCaseParams(userId))
            .onSuccess { result ->
                val user = result.user

                getFavoritesCountUseCase(GetFavoritesCountUseCaseParams(userId)).collect { countResult ->
                    countResult.onSuccess { countUseCaseResult ->
                        updateUIState(ProfileViewState.Content(user, countUseCaseResult.count))
                    }
                }
            }
            .onFailure { exception ->
                updateUIState(ProfileViewState.Error(exception.message ?: "Unknown error"))
                showMessage(exception.message ?: "Unknown error")
            }
    }
}
