package com.app.hatli.core.model

data class CreateProfileInput(
    val role: UserRole,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val gender: Gender,
    val address: UserAddress? = null,
    val nationalId: String? = null,
    /** Local content URI; uploaded only when Storage feature flag is enabled. */
    val idImageLocalUri: String? = null,
    /** Local content URI; uploaded only when Storage feature flag is enabled. */
    val selfieLocalUri: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String? = null,
)
