package com.app.hatli.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.hatli.core.database.converter.InstantConverters
import com.app.hatli.core.database.dao.PostDao
import com.app.hatli.core.database.dao.UserProfileDao
import com.app.hatli.core.database.entity.PostEntity
import com.app.hatli.core.database.entity.UserProfileEntity

@Database(
    entities = [PostEntity::class, UserProfileEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(InstantConverters::class)
abstract class HatliDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userProfileDao(): UserProfileDao
}
