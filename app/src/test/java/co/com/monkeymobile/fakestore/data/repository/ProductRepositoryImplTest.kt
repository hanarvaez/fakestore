package co.com.monkeymobile.fakestore.data.repository

import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.ProductDao
import co.com.monkeymobile.fakestore.data.local.entity.ProductEntity
import co.com.monkeymobile.fakestore.data.remote.api.FakeStoreApi
import co.com.monkeymobile.fakestore.data.remote.dto.ProductDto
import co.com.monkeymobile.fakestore.data.remote.dto.RatingDto
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var api: FakeStoreApi
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var productDao: ProductDao
    private lateinit var repository: ProductRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        api = mockk()
        favoriteDao = mockk()
        productDao = mockk()
        repository = ProductRepositoryImpl(api, favoriteDao, productDao)
    }

    @Test
    fun `getProducts returns products from local DB with favorites`() = runTest {
        val userId = 1
        val localProducts = listOf(
            ProductEntity(1, "Product 1", 10.0, "Desc 1", "cat", "url1", 4.5, 10),
            ProductEntity(2, "Product 2", 20.0, "Desc 2", "cat", "url2", 4.0, 5)
        )

        coEvery { productDao.getAllProducts() } returns flowOf(localProducts)
        coEvery { favoriteDao.getFavoriteProductIds(userId) } returns flowOf(listOf(1))

        val result = repository.getProducts(userId)

        result.collect { products ->
            assertEquals(2, products.size)
            assertTrue(products[0].isFavorite)
            assertFalse(products[1].isFavorite)
        }
    }

    @Test
    fun `getProducts fetches from API when local DB is empty`() = runTest {
        val userId = 1
        val apiProducts = listOf(
            ProductDto(1, "API Product", 15.0, "Desc", "cat", "url", RatingDto(4.0, 10))
        )

        coEvery { productDao.getAllProducts() } returns flowOf(emptyList())
        coEvery { api.getProducts() } returns apiProducts
        coEvery { productDao.insertProducts(any()) } returns Unit
        coEvery { favoriteDao.getFavoriteProductIds(userId) } returns flowOf(emptyList())

        val result = repository.getProducts(userId)

        result.collect { products ->
            assertEquals(1, products.size)
            assertEquals("API Product", products[0].title)
            coVerify { productDao.insertProducts(any()) }
        }
    }

    @Test
    fun `getFavorites returns only favorite products`() = runTest {
        val userId = 1
        val localProducts = listOf(
            ProductEntity(1, "Product 1", 10.0, "Desc 1", "cat", "url1", 4.5, 10),
            ProductEntity(2, "Product 2", 20.0, "Desc 2", "cat", "url2", 4.0, 5)
        )

        coEvery { productDao.getAllProducts() } returns flowOf(localProducts)
        coEvery { favoriteDao.getFavoriteProductIds(userId) } returns flowOf(listOf(1))

        val result = repository.getFavorites(userId)

        result.collect { products ->
            assertEquals(1, products.size)
            assertEquals("Product 1", products[0].title)
            assertTrue(products[0].isFavorite)
        }
    }

    @Test
    fun `getFavoritesCount returns count of favorites for user`() = runTest {
        val userId = 1

        coEvery { favoriteDao.getFavoritesCount(userId) } returns flowOf(5)

        val result = repository.getFavoritesCount(userId)

        result.collect { count ->
            assertEquals(5, count)
        }
    }

    @Test
    fun `addFavorite inserts favorite entity`() = runTest {
        val userId = 1
        val product = Product(1, "Product", 10.0, "Desc", "cat", "url", Rating(4.0, 10), false)

        coEvery { favoriteDao.addFavorite(any()) } returns Unit

        repository.addFavorite(product, userId)

        coVerify { favoriteDao.addFavorite(any()) }
    }

    @Test
    fun `removeFavorite removes favorite by product and user ID`() = runTest {
        val productId = 1
        val userId = 1

        coEvery { favoriteDao.removeFavoriteById(productId, userId) } returns Unit

        repository.removeFavorite(productId, userId)

        coVerify { favoriteDao.removeFavoriteById(productId, userId) }
    }

    @Test
    fun `isFavorite returns true when product is favorite for user`() = runTest {
        val productId = 1
        val userId = 1

        coEvery { favoriteDao.isFavorite(productId, userId) } returns true

        val result = repository.isFavorite(productId, userId)

        assertTrue(result)
    }

    @Test
    fun `isFavorite returns false when product is not favorite for user`() = runTest {
        val productId = 1
        val userId = 1

        coEvery { favoriteDao.isFavorite(productId, userId) } returns false

        val result = repository.isFavorite(productId, userId)

        assertFalse(result)
    }
}
