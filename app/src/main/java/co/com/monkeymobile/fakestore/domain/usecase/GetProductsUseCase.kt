package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : SuspendUseCase<NoParams, GetProductsUseCaseResult>(
    coroutineDispatcher
) {

    override suspend fun execute(parameters: NoParams): GetProductsUseCaseResult {
        val products = repository.getProducts()
        return GetProductsUseCaseResult(products = products)
    }
}

data class GetProductsUseCaseResult(
    val products: List<Product>
)