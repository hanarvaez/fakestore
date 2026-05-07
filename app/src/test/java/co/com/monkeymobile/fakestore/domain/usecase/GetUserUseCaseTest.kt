package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {

    private lateinit var repository: UserRepository

    private lateinit var useCase: GetUserUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUserUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns user`() = runBlocking {
        val user = User(
            id = 8,
            email = "test@test.com",
            username = "testuser",
            password = "password",
            name = Name("John", "Doe"),
            phone = "1234567890",
            v = 0
        )
        coEvery { repository.getUser(8) } returns Result.success(user)

        val result = useCase(8)

        assertTrue(result.isSuccess)
        assertEquals("test@test.com", result.getOrNull()?.email)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runBlocking {
        coEvery { repository.getUser(8) } returns Result.failure(Exception("User not found"))

        val result = useCase(8)

        assertTrue(result.isFailure)
    }
}