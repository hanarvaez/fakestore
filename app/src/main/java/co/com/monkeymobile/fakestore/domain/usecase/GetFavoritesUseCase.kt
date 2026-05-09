package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetFavoritesUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetFavoritesUseCaseParams, GetFavoritesUseCaseResult>(
    coroutineDispatcher
) {

    override fun execute(parameters: GetFavoritesUseCaseParams): Flow<GetFavoritesUseCaseResult> {
        return repository.getFavorites(parameters.userId)
            .map { products ->
                GetFavoritesUseCaseResult(products = products)
            }
    }
}

data class GetFavoritesUseCaseParams(
    val userId: Int
)

data class GetFavoritesUseCaseResult(
    val products: List<Product>
)
