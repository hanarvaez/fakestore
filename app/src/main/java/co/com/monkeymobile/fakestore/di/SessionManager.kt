package co.com.monkeymobile.fakestore.di

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var userId: Int = -1

    fun setUserId(id: Int) {
        userId = id
    }

    fun getUserId(): Int = userId

    fun isLoggedIn(): Boolean = userId != -1

    fun logout() {
        userId = -1
    }
}