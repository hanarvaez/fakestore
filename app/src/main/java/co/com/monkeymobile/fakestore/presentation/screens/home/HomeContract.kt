package co.com.monkeymobile.fakestore.presentation.screens.home

import co.com.monkeymobile.fakestore.domain.model.Product

data class HomeState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null
)

sealed class HomeIntent {
    data object LoadProducts : HomeIntent()
    data class ToggleFavorite(val product: Product) : HomeIntent()
}

sealed class HomeEffect {
    data class ShowError(val message: String) : HomeEffect()
}