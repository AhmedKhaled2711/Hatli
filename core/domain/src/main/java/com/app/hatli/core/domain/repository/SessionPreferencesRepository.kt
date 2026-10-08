package com.app.hatli.core.domain.repository

import com.app.hatli.core.model.UserRole
import kotlinx.coroutines.flow.Flow

interface SessionPreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    val pendingRole: Flow<UserRole?>
    val runnerOnlineBannerDismissed: Flow<Boolean>
    val runnerOnlineBannerShownCount: Flow<Int>
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setPendingRole(role: UserRole?)
    suspend fun clearSessionPreferences()
    suspend fun incrementRunnerOnlineBannerCount()
    suspend fun dismissRunnerOnlineBanner()
}
