package co.com.monkeymobile.fakestore.presentation.screens.home

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.presentation.screens.ViewEvent
import co.com.monkeymobile.fakestore.presentation.screens.ViewState

sealed class HomeViewState : ViewState {

    data object Initial : HomeViewState() {
        override val name: String = "HomeViewState.Initial"
    }

    data object Loading : HomeViewState() {
        override val name: String = "HomeViewState.Loading"
    }

    data class Content(
        val products: List<Product>
    ) : HomeViewState() {
        override val name: String = "HomeViewState.Content"
    }

    data class Error(val message: String) : HomeViewState() {
        override val name: String = "HomeViewState.Error"
    }
}

sealed class HomeViewEvent : ViewEvent {

    data object LoadProducts : HomeViewEvent() {
        override val name: String = "HomeViewEvent.LoadProducts"
    }

    data class ToggleFavorite(val product: Product) : HomeViewEvent() {
        override val name: String = "HomeViewEvent.ToggleFavorite"
    }
}
