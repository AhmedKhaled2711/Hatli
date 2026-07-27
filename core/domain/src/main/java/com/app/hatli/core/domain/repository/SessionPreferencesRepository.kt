package com.app.hatli.core.domain.repository

import com.app.hatli.core.model.UserRole
import kotlinx.coroutines.flow.Flow

interface SessionPreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    val pendingRole: Flow<UserRole?>
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setPendingRole(role: UserRole?)
    suspend fun clearSessionPreferences()
}
