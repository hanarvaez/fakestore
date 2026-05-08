package co.com.monkeymobile.fakestore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT productId FROM favorites WHERE userId = :userId")
    fun getFavoriteProductIds(userId: Int): Flow<List<Int>>

    @Query("SELECT productId FROM favorites WHERE userId = :userId")
    suspend fun getFavoriteProductIdsList(userId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    fun getFavoritesCount(userId: Int): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId AND userId = :userId)")
    suspend fun isFavorite(productId: Int, userId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId AND userId = :userId")
    suspend fun removeFavoriteById(productId: Int, userId: Int)
}