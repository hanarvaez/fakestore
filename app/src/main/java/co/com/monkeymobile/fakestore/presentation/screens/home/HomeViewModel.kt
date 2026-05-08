package co.com.monkeymobile.fakestore.presentation.screens.home

import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.usecase.GetProductsUseCase
import co.com.monkeymobile.fakestore.domain.usecase.NoParams
import co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCase
import co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCaseParams
import co.com.monkeymobile.fakestore.presentation.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : BaseViewModel<HomeViewState, HomeViewEvent>(
    initialState = HomeViewState.Initial
) {

    override fun dispatchViewEvent(event: HomeViewEvent) {
        super.dispatchViewEvent(event)

        viewModelScope.launch {
            when (event) {
                is HomeViewEvent.LoadProducts -> loadProducts()
                is HomeViewEvent.ToggleFavorite -> toggleFavorite(event.product)
            }
        }
    }

    private suspend fun loadProducts() {
        updateUIState(HomeViewState.Loading)

        getProductsUseCase(NoParams)
            .onSuccess { result ->
                updateUIState(HomeViewState.Content(result.products))
            }
            .onFailure { exception ->
                updateUIState(HomeViewState.Error(exception.message ?: "Unknown error"))
                showMessage(exception.message ?: "Unknown error")
            }
    }

    private suspend fun toggleFavorite(product: Product) {
        toggleFavoriteUseCase(ToggleFavoriteUseCaseParams(product))
        loadProducts()
    }
}
