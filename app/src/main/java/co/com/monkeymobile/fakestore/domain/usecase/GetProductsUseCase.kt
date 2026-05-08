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
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProductsUseCaseParams, GetProductsUseCaseResult>(
    coroutineDispatcher
) {

    override fun execute(parameters: GetProductsUseCaseParams): Flow<GetProductsUseCaseResult> {
        return repository.getProducts(parameters.userId).map { products ->
            GetProductsUseCaseResult(products = products)
        }
    }
}

data class GetProductsUseCaseParams(
    val userId: Int
)

data class GetProductsUseCaseResult(
    val products: List<Product>
)
