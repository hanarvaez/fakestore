package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import javax.inject.Inject

class ValidateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        return repository.validateUser(username, password)
    }
}