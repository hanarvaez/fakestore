package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.di.IoDispatcher
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ViewModelScoped
class ValidateCredentialsUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val coroutineDispatcher: CoroutineDispatcher
) : SuspendUseCase<ValidateCredentialsUseCaseParams, ValidateCredentialsUseCaseResult>(
    coroutineDispatcher
) {

    override suspend fun execute(parameters: ValidateCredentialsUseCaseParams): ValidateCredentialsUseCaseResult {
        val user = repository.validateUser(parameters.username, parameters.password)

        return ValidateCredentialsUseCaseResult(user = user)
    }
}

data class ValidateCredentialsUseCaseParams(
    val username: String,
    val password: String
)

data class ValidateCredentialsUseCaseResult(
    val user: User
)
