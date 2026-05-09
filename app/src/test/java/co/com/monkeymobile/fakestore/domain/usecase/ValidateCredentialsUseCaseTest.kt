package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.User
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateCredentialsUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: ValidateCredentialsUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk()
        useCase = ValidateCredentialsUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `invoke returns success with user when credentials are valid`() = runTest {
        val username = "testuser"
        val password = "password123"
        val user = User(
            id = 1,
            email = "test@test.com",
            username = username,
            password = password,
            name = Name("John", "Doe"),
            phone = "1234567890",
            v = 0
        )

        coEvery { repository.validateUser(username, password) } returns user

        val result = useCase(ValidateCredentialsUseCaseParams(username, password))

        result.onSuccess { useCaseResult ->
            assertEquals(username, useCaseResult.user.username)
            assertEquals("test@test.com", useCaseResult.user.email)
        }
    }

    @Test
    fun `invoke returns failure when credentials are invalid`() = runTest {
        val username = "wronguser"
        val password = "wrongpassword"

        coEvery { repository.validateUser(username, password) } throws Exception("Invalid username or password")

        val result = useCase(ValidateCredentialsUseCaseParams(username, password))

        assertTrue(result.isFailure)
    }
}
