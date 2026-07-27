package com.app.hatli.core.database.di

import android.content.Context
import androidx.room.Room
import com.app.hatli.core.database.HatliDatabase
import com.app.hatli.core.database.dao.PostDao
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
        .fallbackToDestructiveMigration(dropAllTables = true) // Phase 0 only — add real migrations before prod data
        .build()

    @Provides
    fun providePostDao(database: HatliDatabase): PostDao = database.postDao()
}
