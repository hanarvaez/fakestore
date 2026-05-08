package co.com.monkeymobile.fakestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.ProductDao
import co.com.monkeymobile.fakestore.data.local.dao.UserDao
import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity
import co.com.monkeymobile.fakestore.data.local.entity.ProductEntity
import co.com.monkeymobile.fakestore.data.local.entity.UserEntity

@Database(
    entities = [FavoriteEntity::class, ProductEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FakeStoreDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
}
