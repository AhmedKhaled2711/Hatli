package com.app.hatli.core.database.di

import android.content.Context
import androidx.room.Room
import com.app.hatli.core.database.HatliDatabase
import com.app.hatli.core.database.dao.PostDao
import com.app.hatli.core.database.dao.UserProfileDao
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
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): HatliDatabase = Room.databaseBuilder(
        context,
        HatliDatabase::class.java,
        "hatli.db",
    )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    fun providePostDao(database: HatliDatabase): PostDao = database.postDao()

    @Provides
    fun provideUserProfileDao(database: HatliDatabase): UserProfileDao = database.userProfileDao()
}
