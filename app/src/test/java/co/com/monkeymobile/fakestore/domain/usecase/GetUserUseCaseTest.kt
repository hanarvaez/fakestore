package co.com.monkeymobile.fakestore.domain.usecase

import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.doReturn

@RunWith(MockitoJUnitRunner::class)
class GetUserUseCaseTest {

    @Mock
    private lateinit var repository: UserRepository

    private lateinit var useCase: GetUserUseCase

    @Before
    fun setup() {
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
        doReturn(Result.success(user)).`when`(repository).getUser(8)

        val result = useCase(8)

        assertTrue(result.isSuccess)
        assertEquals("test@test.com", result.getOrNull()?.email)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runBlocking {
        doReturn(Result.failure<Any>(Exception("User not found"))).`when`(repository).getUser(8)

        val result = useCase(8)

        assertTrue(result.isFailure)
    }
}