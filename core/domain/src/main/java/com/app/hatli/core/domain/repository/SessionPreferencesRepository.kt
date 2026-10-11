package com.app.hatli.core.domain.repository

import com.app.hatli.core.model.AppThemeMode
import com.app.hatli.core.model.UserRole
import kotlinx.coroutines.flow.Flow

interface SessionPreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    val pendingRole: Flow<UserRole?>
    val runnerOnlineBannerDismissed: Flow<Boolean>
    val runnerOnlineBannerShownCount: Flow<Int>
    val themeMode: Flow<AppThemeMode>
    val appLanguage: Flow<String>

    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setPendingRole(role: UserRole?)
    suspend fun clearSessionPreferences()
    suspend fun incrementRunnerOnlineBannerCount()
    suspend fun dismissRunnerOnlineBanner()
    suspend fun setThemeMode(mode: AppThemeMode)
    suspend fun setAppLanguage(languageCode: String)
}
