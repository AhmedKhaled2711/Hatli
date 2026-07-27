package com.app.hatli.core.data.di

import com.app.hatli.core.data.preferences.SessionPreferencesRepositoryImpl
import com.app.hatli.core.data.repository.AuthRepositoryImpl
import com.app.hatli.core.data.repository.PostRepositoryImpl
import com.app.hatli.core.data.repository.UserRepositoryImpl
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.PostRepository
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.repository.UserRepository
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
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSessionPreferencesRepository(
        impl: SessionPreferencesRepositoryImpl,
    ): SessionPreferencesRepository
}
