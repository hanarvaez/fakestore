package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesCountUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getFavoritesCount()
    }
}