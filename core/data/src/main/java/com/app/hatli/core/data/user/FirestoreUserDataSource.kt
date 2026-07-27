package com.app.hatli.core.data.user

import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private val users get() = firestore.collection(COLLECTION)

    fun observe(uid: String): Flow<UserProfile?> = callbackFlow {
        val registration = users.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toUserProfile())
        }
        awaitClose { registration.remove() }
    }

    suspend fun get(uid: String): UserProfile? =
        users.document(uid).get().await().toUserProfile()

    suspend fun upsert(uid: String, profile: UserProfile, isCreate: Boolean) {
        val data = profile.toFirestoreMap(isCreate)
        users.document(uid).set(data, SetOptions.merge()).await()
    }

    suspend fun delete(uid: String) {
        users.document(uid).delete().await()
    }

    private fun DocumentSnapshot.toUserProfile(): UserProfile? {
        if (!exists()) return null
        return UserProfile(
            uid = id,
            role = UserRole.valueOf(getString(FIELD_ROLE) ?: return null),
            phone = getString(FIELD_PHONE).orEmpty(),
            firstName = getString(FIELD_FIRST_NAME).orEmpty(),
            lastName = getString(FIELD_LAST_NAME).orEmpty(),
            email = getString(FIELD_EMAIL).orEmpty(),
            gender = runCatching {
                Gender.valueOf(getString(FIELD_GENDER) ?: Gender.PreferNotToSay.name)
            }.getOrDefault(Gender.PreferNotToSay),
            createdAt = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: getLong(FIELD_CREATED_AT)
                ?: 0L,
            updatedAt = getTimestamp(FIELD_UPDATED_AT)?.toDate()?.time
                ?: getLong(FIELD_UPDATED_AT)
                ?: 0L,
            profileCompleted = getBoolean(FIELD_PROFILE_COMPLETED) ?: false,
            nationalId = getString(FIELD_NATIONAL_ID),
            idImageUrl = getString(FIELD_ID_IMAGE),
            selfieUrl = getString(FIELD_SELFIE),
            transportType = getString(FIELD_TRANSPORT)?.let {
                runCatching { TransportType.valueOf(it) }.getOrNull()
            },
            vehicleInfo = getString(FIELD_VEHICLE),
            verificationStatus = getString(FIELD_VERIFICATION)?.let {
                runCatching { VerificationStatus.valueOf(it) }.getOrNull()
            },
        )
    }

    private fun UserProfile.toFirestoreMap(isCreate: Boolean): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>(
            FIELD_UID to uid,
            FIELD_ROLE to role.name,
            FIELD_PHONE to phone,
            FIELD_FIRST_NAME to firstName,
            FIELD_LAST_NAME to lastName,
            FIELD_EMAIL to email,
            FIELD_GENDER to gender.name,
            FIELD_PROFILE_COMPLETED to profileCompleted,
            FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
            FIELD_NATIONAL_ID to nationalId,
            FIELD_ID_IMAGE to idImageUrl,
            FIELD_SELFIE to selfieUrl,
            FIELD_TRANSPORT to transportType?.name,
            FIELD_VEHICLE to vehicleInfo,
            FIELD_VERIFICATION to verificationStatus?.name,
        )
        if (isCreate) {
            map[FIELD_CREATED_AT] = FieldValue.serverTimestamp()
        }
        return map
    }

    companion object {
        const val COLLECTION = "users"
        const val FIELD_UID = "uid"
        const val FIELD_ROLE = "role"
        const val FIELD_PHONE = "phone"
        const val FIELD_FIRST_NAME = "firstName"
        const val FIELD_LAST_NAME = "lastName"
        const val FIELD_EMAIL = "email"
        const val FIELD_GENDER = "gender"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_PROFILE_COMPLETED = "profileCompleted"
        const val FIELD_NATIONAL_ID = "nationalId"
        const val FIELD_ID_IMAGE = "idImage"
        const val FIELD_SELFIE = "selfie"
        const val FIELD_TRANSPORT = "transportType"
        const val FIELD_VEHICLE = "vehicleInfo"
        const val FIELD_VERIFICATION = "verificationStatus"
    }
}

fun CreateProfileInput.toDraftProfile(
    uid: String,
    idImageUrl: String?,
    selfieUrl: String?,
    now: Long = System.currentTimeMillis(),
): UserProfile = UserProfile(
    uid = uid,
    role = role,
    phone = phone,
    firstName = firstName.trim(),
    lastName = lastName.trim(),
    email = email.trim(),
    gender = gender,
    createdAt = now,
    updatedAt = now,
    profileCompleted = true,
    nationalId = nationalId?.trim(),
    idImageUrl = idImageUrl,
    selfieUrl = selfieUrl,
    transportType = transportType,
    vehicleInfo = vehicleInfo?.trim(),
    verificationStatus = if (role == UserRole.Runner) VerificationStatus.Pending else null,
)
