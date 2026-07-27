package com.app.hatli.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "hatli_session",
)

@Singleton
class SessionPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : SessionPreferencesRepository {

    private val dataStore = context.sessionDataStore

    override val onboardingCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING] ?: false
    }

    override val pendingRole: Flow<UserRole?> = dataStore.data.map { prefs ->
        prefs[KEY_PENDING_ROLE]?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[KEY_ONBOARDING] = completed }
    }

    override suspend fun setPendingRole(role: UserRole?) {
        dataStore.edit { prefs ->
            if (role == null) prefs.remove(KEY_PENDING_ROLE)
            else prefs[KEY_PENDING_ROLE] = role.name
        }
    }

    override suspend fun clearSessionPreferences() {
        dataStore.edit { prefs ->
            // Keep onboarding_completed — user should not see intro again after logout.
            prefs.remove(KEY_PENDING_ROLE)
        }
    }

    private companion object {
        val KEY_ONBOARDING = booleanPreferencesKey("onboarding_completed")
        val KEY_PENDING_ROLE = stringPreferencesKey("pending_role")
    }
}
