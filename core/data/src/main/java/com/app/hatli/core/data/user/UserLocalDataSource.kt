package com.app.hatli.core.data.user

import com.app.hatli.core.data.mapper.toDomain
import com.app.hatli.core.data.mapper.toEntity
import com.app.hatli.core.database.dao.UserProfileDao
import com.app.hatli.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    private val dao: UserProfileDao,
) {
    fun observe(uid: String): Flow<UserProfile?> =
        dao.observeByUid(uid).map { it?.toDomain() }

    suspend fun get(uid: String): UserProfile? = dao.getByUid(uid)?.toDomain()

    suspend fun upsert(profile: UserProfile) = dao.upsert(profile.toEntity())

    suspend fun delete(uid: String) = dao.deleteByUid(uid)

    suspend fun clear() = dao.clearAll()
}
