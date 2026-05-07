package co.com.monkeymobile.fakestore.data.remote.api

import co.com.monkeymobile.fakestore.data.remote.dto.ProductDto
import co.com.monkeymobile.fakestore.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): UserDto

    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
