package com.app.hatli.core.model

/**
 * Domain user profile. Runner-only fields are null for Requesters.
 * Document ID in Firestore must equal [uid] (Firebase Auth UID).
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
    val idImageUrl: String? = null,
    val selfieUrl: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String? = null,
    val verificationStatus: VerificationStatus? = null,
) {
    val fullName: String get() = "$firstName $lastName".trim()
}
