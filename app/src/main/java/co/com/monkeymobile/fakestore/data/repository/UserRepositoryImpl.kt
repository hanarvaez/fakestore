package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.UserDao
import co.com.monkeymobile.fakestore.data.mapper.toDomain
import co.com.monkeymobile.fakestore.data.mapper.toEntity
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUser(userId: Int): Result<User> {
        return try {
            val user = api.getUser(userId).toDomain()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun validateUser(username: String, password: String): Result<User> {
        return try {
            val count = userDao.getUsersCount()

            if (count == 0) {
                val users = api.getUsers()
                val entities = users.map { it.toEntity() }
                userDao.insertUsers(entities)
            }

            val userEntity = userDao.validateUser(username, password)
            if (userEntity != null) {
                Result.success(userEntity.toDomain())
            } else {
                Result.failure(Exception("Invalid username or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
