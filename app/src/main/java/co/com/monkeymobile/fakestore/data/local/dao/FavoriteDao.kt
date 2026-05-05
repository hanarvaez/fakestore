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
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
    
    @Query("SELECT * FROM favorites")
    suspend fun getAllFavoritesList(): List<FavoriteEntity>
    
    @Query("SELECT id FROM favorites")
    suspend fun getAllFavoriteIds(): List<Int>
    
    @Query("SELECT COUNT(*) FROM favorites")
    fun getFavoritesCount(): Flow<Int>
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :productId)")
    suspend fun isFavorite(productId: Int): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(product: FavoriteEntity)
    
    @Delete
    suspend fun removeFavorite(product: FavoriteEntity)
    
    @Query("DELETE FROM favorites WHERE id = :productId")
    suspend fun removeFavoriteById(productId: Int)
}