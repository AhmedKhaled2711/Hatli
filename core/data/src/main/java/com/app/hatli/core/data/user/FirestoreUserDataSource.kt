package com.app.hatli.core.data.user

import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserAddress
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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
                // After Auth.signOut(), rules deny the listen. End cleanly — do not crash UI.
                if (error is FirebaseFirestoreException &&
                    error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
                ) {
                    trySend(null)
                    close()
                } else {
                    close(error)
                }
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
                Gender.valueOf(getString(FIELD_GENDER) ?: Gender.Male.name)
            }.getOrDefault(Gender.Male),
            createdAt = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: getLong(FIELD_CREATED_AT)
                ?: 0L,
            updatedAt = getTimestamp(FIELD_UPDATED_AT)?.toDate()?.time
                ?: getLong(FIELD_UPDATED_AT)
                ?: 0L,
            profileCompleted = getBoolean(FIELD_PROFILE_COMPLETED) ?: false,
            address = readAddress(),
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

    @Suppress("UNCHECKED_CAST")
    private fun DocumentSnapshot.readAddress(): UserAddress? {
        val map = get(FIELD_ADDRESS) as? Map<String, Any?> ?: return null
        val city = map[ADDRESS_CITY] as? String
        val area = map[ADDRESS_AREA] as? String
        val street = map[ADDRESS_STREET] as? String
        val building = map[ADDRESS_BUILDING] as? String
        if (city.isNullOrBlank() && area.isNullOrBlank() &&
            street.isNullOrBlank() && building.isNullOrBlank()
        ) {
            return null
        }
        return UserAddress(
            city = city.orEmpty(),
            area = area.orEmpty(),
            street = street.orEmpty(),
            building = building.orEmpty(),
            floor = map[ADDRESS_FLOOR] as? String,
            apartment = map[ADDRESS_APARTMENT] as? String,
            notes = map[ADDRESS_NOTES] as? String,
            latitude = (map[ADDRESS_LAT] as? Number)?.toDouble(),
            longitude = (map[ADDRESS_LNG] as? Number)?.toDouble(),
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
            FIELD_ADDRESS to address?.toFirestoreMap(),
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

    private fun UserAddress.toFirestoreMap(): Map<String, Any?> = mapOf(
        ADDRESS_CITY to city,
        ADDRESS_AREA to area,
        ADDRESS_STREET to street,
        ADDRESS_BUILDING to building,
        ADDRESS_FLOOR to floor,
        ADDRESS_APARTMENT to apartment,
        ADDRESS_NOTES to notes,
        ADDRESS_LAT to latitude,
        ADDRESS_LNG to longitude,
    )

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
        const val FIELD_ADDRESS = "address"
        const val ADDRESS_CITY = "city"
        const val ADDRESS_AREA = "area"
        const val ADDRESS_STREET = "street"
        const val ADDRESS_BUILDING = "building"
        const val ADDRESS_FLOOR = "floor"
        const val ADDRESS_APARTMENT = "apartment"
        const val ADDRESS_NOTES = "notes"
        const val ADDRESS_LAT = "latitude"
        const val ADDRESS_LNG = "longitude"
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
    address = address?.let {
        it.copy(
            city = it.city.trim(),
            area = it.area.trim(),
            street = it.street.trim(),
            building = it.building.trim(),
            floor = it.floor?.trim()?.ifBlank { null },
            apartment = it.apartment?.trim()?.ifBlank { null },
            notes = it.notes?.trim()?.ifBlank { null },
        )
    },
    nationalId = nationalId?.trim(),
    idImageUrl = idImageUrl,
    selfieUrl = selfieUrl,
    transportType = transportType,
    vehicleInfo = vehicleInfo?.trim(),
    verificationStatus = if (role == UserRole.Runner) VerificationStatus.Pending else null,
)
