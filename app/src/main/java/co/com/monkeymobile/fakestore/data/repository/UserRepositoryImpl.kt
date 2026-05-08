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

    override suspend fun getUser(userId: Int): User {
        return try {
            api.getUser(userId).toDomain()
        } catch (e: Exception) {
            throw Exception("Failed to get user: ${e.message}")
        }
    }

    override suspend fun validateUser(username: String, password: String): User {
        val count = userDao.getUsersCount()

        if (count == 0) {
            val users = api.getUsers()
            val entities = users.map { it.toEntity() }
            userDao.insertUsers(entities)
        }

        val userEntity = userDao.validateUser(username, password)
            ?: throw Exception("Invalid username or password")

        return userEntity.toDomain()
    }
}
