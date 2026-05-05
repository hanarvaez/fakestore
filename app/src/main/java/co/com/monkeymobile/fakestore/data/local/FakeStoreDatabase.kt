package co.com.monkeymobile.fakestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FakeStoreDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}