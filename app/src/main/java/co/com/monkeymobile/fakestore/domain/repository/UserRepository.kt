package co.com.monkeymobile.fakestore.domain.repository

import co.com.monkeymobile.fakestore.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: Int): Result<User>
    suspend fun validateUser(username: String, password: String): Result<User>
}