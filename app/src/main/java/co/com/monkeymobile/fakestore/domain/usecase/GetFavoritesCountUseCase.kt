package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetFavoritesCountUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<NoParams, GetFavoritesCountUseCaseResult>(
    coroutineDispatcher
) {

    override fun execute(parameters: NoParams): Flow<GetFavoritesCountUseCaseResult> {
        return repository.getFavoritesCount().map { count ->
            GetFavoritesCountUseCaseResult(count = count)
        }
    }
}

data class GetFavoritesCountUseCaseResult(
    val count: Int
)
