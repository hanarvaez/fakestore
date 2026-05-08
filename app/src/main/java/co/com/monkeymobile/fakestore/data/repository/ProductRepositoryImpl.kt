package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.ProductDao
import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity
import co.com.monkeymobile.fakestore.data.local.entity.ProductEntity
import co.com.monkeymobile.fakestore.data.mapper.toDomain
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val favoriteDao: FavoriteDao,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(userId: Int): Flow<List<Product>> {
        val productsFlow = productDao.getAllProducts().map { entities ->
            entities.ifEmpty {
                val productsFromApi = api.getProducts()
                val entitiesMapped = productsFromApi.map { dto ->
                    ProductEntity(
                        id = dto.id,
                        title = dto.title,
                        price = dto.price,
                        description = dto.description,
                        category = dto.category,
                        image = dto.image,
                        rate = dto.rating.rate,
                        count = dto.rating.count
                    )
                }
                productDao.insertProducts(entitiesMapped)
                entitiesMapped
            }
        }

        return productsFlow.combine(favoriteDao.getFavoriteProductIds(userId)) { products, favoriteIds ->
            products.map { entity ->
                entity.toDomain(isFavorite = entity.id in favoriteIds)
            }
        }
    }

    override fun getFavorites(userId: Int): Flow<List<Product>> {
        return favoriteDao.getFavoriteProductIds(userId).map { favoriteIds ->
            val products = productDao.getAllProducts().first()
            products.filter { it.id in favoriteIds }.map { it.toDomain(isFavorite = true) }
        }
    }

    override fun getFavoritesCount(userId: Int): Flow<Int> {
        return favoriteDao.getFavoritesCount(userId)
    }

    override suspend fun addFavorite(product: Product, userId: Int) {
        favoriteDao.addFavorite(FavoriteEntity(productId = product.id, userId = userId))
    }

    override suspend fun removeFavorite(productId: Int, userId: Int) {
        favoriteDao.removeFavoriteById(productId, userId)
    }

    override suspend fun isFavorite(productId: Int, userId: Int): Boolean {
        return favoriteDao.isFavorite(productId, userId)
    }
}