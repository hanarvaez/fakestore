package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : SuspendUseCase<ToggleFavoriteUseCaseParams, ToggleFavoriteUseCaseResult>(
    coroutineDispatcher
) {

    override suspend fun execute(parameters: ToggleFavoriteUseCaseParams): ToggleFavoriteUseCaseResult {
        val product = parameters.product

        if (repository.isFavorite(product.id)) {
            repository.removeFavorite(product.id)
        } else {
            repository.addFavorite(product)
        }

        return ToggleFavoriteUseCaseResult(isFavorite = !product.isFavorite)
    }
}

data class ToggleFavoriteUseCaseParams(
    val product: Product
)

data class ToggleFavoriteUseCaseResult(
    val isFavorite: Boolean
)