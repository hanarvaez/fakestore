package co.com.monkeymobile.fakestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.UserDao
import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity
import co.com.monkeymobile.fakestore.data.local.entity.UserEntity

@Database(
    entities = [FavoriteEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FakeStoreDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): UserDao
}