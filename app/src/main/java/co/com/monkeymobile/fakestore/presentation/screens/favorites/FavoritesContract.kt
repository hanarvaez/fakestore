package co.com.monkeymobile.fakestore.presentation.screens.favorites

import co.com.monkeymobile.fakestore.domain.model.Product

data class FavoritesState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList()
)

sealed class FavoritesIntent {
    data object LoadFavorites : FavoritesIntent()
    data class RemoveFavorite(val product: Product) : FavoritesIntent()
}

sealed class FavoritesEffect {
    data class ShowError(val message: String) : FavoritesEffect()
}