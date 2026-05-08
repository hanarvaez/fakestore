package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.UserDao
import co.com.monkeymobile.fakestore.data.local.entity.UserEntity
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.data.remote.dto.NameDto
import co.com.monkeymobile.fakestore.data.remote.dto.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var api: FakeStoreApi
    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        userDao = mockk()
        repository = UserRepositoryImpl(api, userDao)
    }

    @Test
    fun `getUser returns user from local DB when exists`() = runTest {
        val userId = 1
        val localUser = UserEntity(userId, "test@test.com", "testuser", "pass", "John", "Doe", "123", 0)

        coEvery { userDao.getUserById(userId) } returns localUser

        val result = repository.getUser(userId)

        assertEquals("testuser", result.username)
        assertEquals("test@test.com", result.email)
        coVerify(exactly = 0) { api.getUser(any()) }
    }

    @Test
    fun `getUser fetches from API and saves when not in local DB`() = runTest {
        val userId = 1
        val userDto = UserDto(userId, "test@test.com", "testuser", "pass", NameDto("John", "Doe"), "123", 0)

        coEvery { userDao.getUserById(userId) } returns null
        coEvery { api.getUser(userId) } returns userDto
        coEvery { userDao.insertUser(any()) } returns Unit

        val result = repository.getUser(userId)

        assertEquals("testuser", result.username)
        coVerify { userDao.insertUser(any()) }
    }

    @Test
    fun `getUser throws exception when API fails and not in local DB`() = runTest {
        val userId = 1

        coEvery { userDao.getUserById(userId) } returns null
        coEvery { api.getUser(userId) } throws Exception("Network error")

        val result = try {
            repository.getUser(userId)
            null
        } catch (e: Exception) {
            e
        }

        assertTrue(result != null)
        assertTrue(result!!.message!!.contains("Failed to get user"))
    }

    @Test
    fun `validateUser syncs users from API when local DB is empty`() = runTest {
        val username = "testuser"
        val password = "password123"
        val apiUsers = listOf(
            UserDto(1, "test@test.com", username, password, NameDto("John", "Doe"), "123", 0)
        )
        val userEntity = UserEntity(1, "test@test.com", username, password, "John", "Doe", "123", 0)

        coEvery { userDao.getUsersCount() } returns 0
        coEvery { api.getUsers() } returns apiUsers
        coEvery { userDao.insertUsers(any()) } returns Unit
        coEvery { userDao.validateUser(username, password) } returns userEntity

        val result = repository.validateUser(username, password)

        assertEquals(username, result.username)
        coVerify { api.getUsers() }
        coVerify { userDao.insertUsers(any()) }
    }

    @Test
    fun `validateUser validates credentials from local DB when users exist`() = runTest {
        val username = "testuser"
        val password = "password123"
        val userEntity = UserEntity(1, "test@test.com", username, password, "John", "Doe", "123", 0)

        coEvery { userDao.getUsersCount() } returns 5
        coEvery { userDao.validateUser(username, password) } returns userEntity

        val result = repository.validateUser(username, password)

        assertEquals(username, result.username)
        coVerify(exactly = 0) { api.getUsers() }
    }

    @Test
    fun `validateUser throws exception when credentials are invalid`() = runTest {
        val username = "wronguser"
        val password = "wrongpassword"

        coEvery { userDao.getUsersCount() } returns 1
        coEvery { userDao.validateUser(username, password) } returns null

        val result = try {
            repository.validateUser(username, password)
            null
        } catch (e: Exception) {
            e
        }

        assertTrue(result != null)
        assertTrue(result!!.message!!.contains("Invalid username or password"))
    }
}
