package com.app.hatli.core.data.repository

import com.app.hatli.core.common.dispatcher.IoDispatcher
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.feature.HatliFeatureFlags
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.data.auth.FirebaseAuthDataSource
import com.app.hatli.core.data.auth.FirebaseErrorMapper
import com.app.hatli.core.data.user.FirebaseStorageDataSource
import com.app.hatli.core.data.user.FirestoreUserDataSource
import com.app.hatli.core.data.user.UserLocalDataSource
import com.app.hatli.core.data.user.toDraftProfile
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remote: FirestoreUserDataSource,
    private val local: UserLocalDataSource,
    // Kept injected so Storage uploads can be re-enabled via HatliFeatureFlags only.
    private val storage: FirebaseStorageDataSource,
    private val authDataSource: FirebaseAuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    override fun observeProfile(uid: String): Flow<UserProfile?> = flow {
        local.get(uid)?.let { emit(it) }
        emitAll(
            remote.observe(uid).onEach { profile ->
                if (profile != null) local.upsert(profile)
            },
        )
    }.catch {
        // Listener may fail after logout / rule denial — keep UI alive with last-known null.
        emit(null)
    }

    override suspend fun getProfile(uid: String): Result<UserProfile?> =
        withContext(ioDispatcher) {
            val cached = local.get(uid)
            if (cached != null) {
                return@withContext Result.Success(cached)
            }
            try {
                val remoteProfile = remote.get(uid)
                if (remoteProfile != null) {
                    local.upsert(remoteProfile)
                    Result.Success(remoteProfile)
                } else {
                    Result.Success(null)
                }
            } catch (t: Throwable) {
                Result.Error(FirebaseErrorMapper.map(t))
            }
        }

    override suspend fun createProfile(input: CreateProfileInput): Result<UserProfile> =
        withContext(ioDispatcher) {
            try {
                validate(input)
                val uid = authDataSource.currentUser()?.uid
                    ?: return@withContext Result.Error(AppError.Unauthorized())

                // Spark plan: skip Storage. Re-enable with HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED.
                val idUrl: String?
                val selfieUrl: String?
                if (HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED) {
                    idUrl = input.idImageLocalUri?.let { storage.uploadIdImage(uid, it) }
                    selfieUrl = input.selfieLocalUri?.let { storage.uploadSelfie(uid, it) }
                } else {
                    idUrl = null
                    selfieUrl = null
                }

                val profile = input.toDraftProfile(uid, idUrl, selfieUrl)
                remote.upsert(uid, profile, isCreate = true)
                local.upsert(profile)
                Result.Success(profile)
            } catch (t: Throwable) {
                Result.Error(FirebaseErrorMapper.map(t))
            }
        }

    override suspend fun updateProfile(profile: UserProfile): Result<UserProfile> =
        withContext(ioDispatcher) {
            try {
                val updated = profile.copy(updatedAt = System.currentTimeMillis())
                remote.upsert(updated.uid, updated, isCreate = false)
                local.upsert(updated)
                Result.Success(updated)
            } catch (t: Throwable) {
                Result.Error(FirebaseErrorMapper.map(t))
            }
        }

    override suspend fun deleteProfile(uid: String): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                remote.delete(uid)
                local.delete(uid)
                Result.Success(Unit)
            } catch (t: Throwable) {
                Result.Error(FirebaseErrorMapper.map(t))
            }
        }

    override suspend fun clearLocalCache() = withContext(ioDispatcher) {
        local.clear()
    }

    private fun validate(input: CreateProfileInput) {
        if (input.firstName.isBlank() || input.lastName.isBlank()) {
            throw AppError.InvalidInput("First and last name are required.")
        }
        if (input.email.isNotBlank() && !input.email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
            throw AppError.InvalidInput("Enter a valid email address.")
        }
        if (input.phone.isBlank()) {
            throw AppError.InvalidInput("Phone number is required.")
        }
        val address = input.address
        if (address == null ||
            address.city.isBlank() ||
            address.area.isBlank() ||
            address.street.isBlank() ||
            address.building.isBlank()
        ) {
            throw AppError.InvalidInput("City, area, street, and building are required.")
        }
        if (input.role == UserRole.Runner) {
            if (input.nationalId.isNullOrBlank()) {
                throw AppError.InvalidInput("National ID is required for runners.")
            }
            // Image uploads require Firebase Storage (Blaze). Optional while disabled.
            if (HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED) {
                if (input.idImageLocalUri.isNullOrBlank() || input.selfieLocalUri.isNullOrBlank()) {
                    throw AppError.InvalidInput("ID image and selfie are required for runners.")
                }
            }
            if (input.transportType == null) {
                throw AppError.InvalidInput("Select a transport type.")
            }
            val needsVehicle = input.transportType != TransportType.Walking
            if (needsVehicle && input.vehicleInfo.isNullOrBlank()) {
                throw AppError.InvalidInput("Vehicle info is required.")
            }
        }
    }
}
