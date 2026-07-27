package com.app.hatli.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.hatli.core.database.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE uid = :uid LIMIT 1")
    fun observeByUid(uid: String): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE uid = :uid LIMIT 1")
    suspend fun getByUid(uid: String): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("DELETE FROM user_profiles WHERE uid = :uid")
    suspend fun deleteByUid(uid: String)

    @Query("DELETE FROM user_profiles")
    suspend fun clearAll()
}
