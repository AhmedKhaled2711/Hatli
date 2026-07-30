package com.app.hatli.core.model

/**
 * Domain user profile. Runner-only fields are null for Requesters.
 * Document ID in Firestore must equal [uid] (Firebase Auth UID).
 *
 * [idImageUrl] / [selfieUrl] remain optional placeholders until Firebase Storage
 * (Blaze) is enabled via [com.app.hatli.core.common.feature.HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED].
 */
data class UserProfile(
    val uid: String,
    val role: UserRole,
    val phone: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender,
    val createdAt: Long,
    val updatedAt: Long,
    val profileCompleted: Boolean,
    // Runner
    val nationalId: String? = null,
    /** Remote Storage URL when uploads are enabled; otherwise null placeholder. */
    val idImageUrl: String? = null,
    /** Remote Storage URL when uploads are enabled; otherwise null placeholder. */
    val selfieUrl: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String? = null,
    val verificationStatus: VerificationStatus? = null,
) {
    val fullName: String get() = "$firstName $lastName".trim()
}
