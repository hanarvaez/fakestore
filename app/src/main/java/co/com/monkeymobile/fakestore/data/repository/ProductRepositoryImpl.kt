package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.ProductDao
import co.com.monkeymobile.fakestore.data.local.entity.ProductEntity
import co.com.monkeymobile.fakestore.data.mapper.toDomain
import co.com.monkeymobile.fakestore.data.mapper.toEntity
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
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

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getProductsCount().map { count ->
            if (count == 0) {
                val productsFromApi = api.getProducts()
                val entities = productsFromApi.map { dto ->
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
                productDao.insertProducts(entities)
            }
        }.map {
            val favoriteIds = favoriteDao.getAllFavoriteIds().toSet()

            productDao.getAllProducts().first().map { entity ->
                entity.toDomain(isFavorite = entity.id in favoriteIds)
            }
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
