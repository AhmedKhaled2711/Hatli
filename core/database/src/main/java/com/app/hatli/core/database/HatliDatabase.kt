package com.app.hatli.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.hatli.core.database.converter.InstantConverters
import com.app.hatli.core.database.dao.UserProfileDao
import com.app.hatli.core.database.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class],
    version = 4,
    exportSchema = false,
)
@TypeConverters(InstantConverters::class)
abstract class HatliDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}
