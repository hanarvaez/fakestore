package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.mapper.toDomain
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi
) : UserRepository {

    override suspend fun getUser(userId: Int): Result<User> {
        return try {
            val user = api.getUser(userId).toDomain()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}