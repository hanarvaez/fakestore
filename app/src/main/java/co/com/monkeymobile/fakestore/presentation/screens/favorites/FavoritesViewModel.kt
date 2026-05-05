package co.com.monkeymobile.fakestore.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.usecase.GetFavoritesUseCase
import co.com.monkeymobile.fakestore.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FavoritesEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleIntent(FavoritesIntent.LoadFavorites)
    }

    fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.LoadFavorites -> loadFavorites()
            is FavoritesIntent.RemoveFavorite -> removeFavorite(intent.product)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            getFavoritesUseCase().collect { products ->
                _state.update { it.copy(isLoading = false, products = products) }
            }
        }
    }

    private fun removeFavorite(product: Product) {
        viewModelScope.launch {
            toggleFavoriteUseCase(product)
            loadFavorites()
        }
    }
}