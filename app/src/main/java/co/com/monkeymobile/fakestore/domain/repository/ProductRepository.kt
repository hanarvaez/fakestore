package co.com.monkeymobile.fakestore.domain.repository

import co.com.monkeymobile.fakestore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(userId: Int): Flow<List<Product>>
    fun getFavorites(userId: Int): Flow<List<Product>>
    fun getFavoritesCount(userId: Int): Flow<Int>
    suspend fun addFavorite(product: Product, userId: Int)
    suspend fun removeFavorite(productId: Int, userId: Int)
    suspend fun isFavorite(productId: Int, userId: Int): Boolean
}
