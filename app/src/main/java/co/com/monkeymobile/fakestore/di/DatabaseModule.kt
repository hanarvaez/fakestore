package co.com.monkeymobile.fakestore.di

import android.content.Context
import androidx.room.Room
import co.com.monkeymobile.fakestore.data.local.FakeStoreDatabase
import co.com.monkeymobile.fakestore.data.local.dao.FavoriteDao
import co.com.monkeymobile.fakestore.data.local.dao.ProductDao
import co.com.monkeymobile.fakestore.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FakeStoreDatabase {
        return Room.databaseBuilder(
            context,
            FakeStoreDatabase::class.java,
            "fakestore_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: FakeStoreDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: FakeStoreDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: FakeStoreDatabase): ProductDao {
        return database.productDao()
    }
}
