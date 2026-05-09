package co.com.monkeymobile.fakestore.presentation.screens.favorites

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.presentation.screens.ViewEvent
import co.com.monkeymobile.fakestore.presentation.screens.ViewState

sealed class FavoritesViewState : ViewState {

    data object Initial : FavoritesViewState() {
        override val name: String = "FavoritesViewState.Initial"
    }

    data object Loading : FavoritesViewState() {
        override val name: String = "FavoritesViewState.Loading"
    }

    data class Content(
        val products: List<Product>
    ) : FavoritesViewState() {
        override val name: String = "FavoritesViewState.Content"
    }

    data object Empty : FavoritesViewState() {
        override val name: String = "FavoritesViewState.Empty"
    }
}

sealed class FavoritesViewEvent : ViewEvent {

    data object LoadFavorites : FavoritesViewEvent() {
        override val name: String = "FavoritesViewEvent.LoadFavorites"
    }

    data class RemoveFavorite(val product: Product) : FavoritesViewEvent() {
        override val name: String = "FavoritesViewEvent.RemoveFavorite"
    }
}
