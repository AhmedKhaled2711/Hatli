package com.app.hatli.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.hatli.core.database.HatliDatabase
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

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS posts")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): HatliDatabase = Room.databaseBuilder(
        context,
        HatliDatabase::class.java,
        "hatli.db",
    )
        .addMigrations(MIGRATION_3_4)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    fun provideUserProfileDao(database: HatliDatabase): UserProfileDao = database.userProfileDao()
}
