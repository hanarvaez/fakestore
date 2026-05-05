package co.com.monkeymobile.fakestore.di

import co.com.monkeymobile.fakestore.data.repository.ProductRepositoryImpl
import co.com.monkeymobile.fakestore.data.repository.UserRepositoryImpl
import co.com.monkeymobile.fakestore.domain.repository.ProductRepository
import co.com.monkeymobile.fakestore.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}