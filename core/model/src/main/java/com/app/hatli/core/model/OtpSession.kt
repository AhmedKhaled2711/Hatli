package com.app.hatli.core.model

/**
 * Opaque OTP session returned after sendOtp — never expose Firebase ForceResendingToken to UI.
 */
data class OtpSession(
    val verificationId: String,
    val phoneNumber: String,
)
