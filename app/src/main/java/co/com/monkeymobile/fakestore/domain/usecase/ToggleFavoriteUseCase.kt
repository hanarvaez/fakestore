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
) : SuspendUseCase<ToggleFavoriteUseCaseParams, NoResult>(
    coroutineDispatcher
) {

    override suspend fun execute(parameters: ToggleFavoriteUseCaseParams): NoResult {
        val product = parameters.product

        if (repository.isFavorite(product.id, parameters.userId)) {
            repository.removeFavorite(product.id, parameters.userId)
        } else {
            repository.addFavorite(product, parameters.userId)
        }

        return NoResult
    }
}

data class ToggleFavoriteUseCaseParams(
    val product: Product,
    val userId: Int
)
