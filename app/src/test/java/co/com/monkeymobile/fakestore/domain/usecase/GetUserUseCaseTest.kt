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
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetUserUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUserUseCase(repository, UnconfinedTestDispatcher())
    }

    @Test
    fun `test get user returns success`() = runTest {
        val userId = 8
        val user = User(
            id = userId,
            email = "test@test.com",
            username = "testuser",
            password = "password",
            name = Name("John", "Doe"),
            phone = "1234567890",
            v = 0
        )

        coEvery { repository.getUser(userId) } returns user

        val result = useCase(GetUserUseCaseParams(userId))

        result.onSuccess { useCaseResult ->
            assertEquals("test@test.com", useCaseResult.user.email)
        }
    }
}
