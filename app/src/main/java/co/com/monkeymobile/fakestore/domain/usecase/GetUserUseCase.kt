package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : SuspendUseCase<GetUserUseCaseParams, GetUserUseCaseResult>(
    coroutineDispatcher
) {

    override suspend fun execute(parameters: GetUserUseCaseParams): GetUserUseCaseResult {
        val user = repository.getUser(parameters.userId).getOrThrow()
        return GetUserUseCaseResult(user = user)
    }
}

data class GetUserUseCaseParams(
    val userId: Int
)

data class GetUserUseCaseResult(
    val user: User
)
