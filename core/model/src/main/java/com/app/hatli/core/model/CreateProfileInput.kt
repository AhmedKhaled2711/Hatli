package com.app.hatli.core.model

data class CreateProfileInput(
    val role: UserRole,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val gender: Gender,
    val nationalId: String? = null,
    val idImageLocalUri: String? = null,
    val selfieLocalUri: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String? = null,
)
