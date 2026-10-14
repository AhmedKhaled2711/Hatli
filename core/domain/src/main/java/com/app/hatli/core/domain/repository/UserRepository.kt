package com.app.hatli.core.domain.repository

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeProfile(uid: String): Flow<UserProfile?>
    suspend fun getProfile(uid: String, forceRefresh: Boolean = false): Result<UserProfile?>
    suspend fun createProfile(input: CreateProfileInput): Result<UserProfile>
    suspend fun updateProfile(profile: UserProfile): Result<UserProfile>
    suspend fun deleteProfile(uid: String): Result<Unit>
    suspend fun clearLocalCache()
}
