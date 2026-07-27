package com.app.hatli.core.model

/**
 * Authenticated identity from Firebase Auth — no Firestore profile fields.
 */
data class AuthUser(
    val uid: String,
    val phoneNumber: String?,
)
