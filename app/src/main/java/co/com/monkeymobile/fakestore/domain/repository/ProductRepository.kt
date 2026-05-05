package co.com.monkeymobile.fakestore.domain.repository

import co.com.monkeymobile.fakestore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    fun getFavorites(): Flow<List<Product>>
    fun getFavoritesCount(): Flow<Int>
    suspend fun addFavorite(product: Product)
    suspend fun removeFavorite(productId: Int)
    suspend fun isFavorite(productId: Int): Boolean
}