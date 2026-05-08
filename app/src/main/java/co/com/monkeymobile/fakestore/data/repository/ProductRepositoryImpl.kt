package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.mapper.toDomain
import co.com.monkeymobile.fakestore.data.mapper.toEntity
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val favoriteDao: FavoriteDao
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return try {
            val favoriteIds = favoriteDao.getAllFavoriteIds().toSet()
            api.getProducts().map { dto ->
                dto.toDomain(isFavorite = dto.id in favoriteIds)
            }
        } catch (e: Exception) {
            throw Exception("Failed to get products: ${e.message}")
        }
    }

    override fun getFavorites(): Flow<List<Product>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavoritesCount(): Flow<Int> {
        return favoriteDao.getFavoritesCount()
    }

    override suspend fun addFavorite(product: Product) {
        favoriteDao.addFavorite(product.toEntity())
    }

    override suspend fun removeFavorite(productId: Int) {
        favoriteDao.removeFavoriteById(productId)
    }

    override suspend fun isFavorite(productId: Int): Boolean {
        return favoriteDao.isFavorite(productId)
    }
}