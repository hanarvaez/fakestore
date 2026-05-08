package co.com.monkeymobile.fakestore.presentation.screens.favorites

import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesUseCase
import co.com.monkeymobile.fakestore.domain.usecase.NoParams
import co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCase
import co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCaseParams
import co.com.monkeymobile.fakestore.presentation.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : BaseViewModel<FavoritesViewState, FavoritesViewEvent>(
    initialState = FavoritesViewState.Initial
) {

    override fun dispatchViewEvent(event: FavoritesViewEvent) {
        super.dispatchViewEvent(event)

        viewModelScope.launch {
            when (event) {
                is FavoritesViewEvent.LoadFavorites -> loadFavorites()
                is FavoritesViewEvent.RemoveFavorite -> removeFavorite(event.product)
            }
        }
    }

    private suspend fun loadFavorites() {
        updateUIState(FavoritesViewState.Loading)

        getFavoritesUseCase(NoParams).collect { result ->
            result.onSuccess { useCaseResult ->
                val products = useCaseResult.products

                if (products.isEmpty()) {
                    updateUIState(FavoritesViewState.Empty)
                } else {
                    updateUIState(FavoritesViewState.Content(products))
                }
            }.onFailure { exception ->
                updateUIState(FavoritesViewState.Empty)
                showMessage(exception.message ?: "Failed to load favorites")
            }
        }
    }

    private suspend fun removeFavorite(product: Product) {
        toggleFavoriteUseCase(ToggleFavoriteUseCaseParams(product))
        loadFavorites()
    }
}
