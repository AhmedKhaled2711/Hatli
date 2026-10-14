package com.app.hatli.core.data.auth

import android.util.Log
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.data.firebase.AuthActivityHolder
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.OtpSession
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "PhoneAuth"

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val activityHolder: AuthActivityHolder,
) {
    private val forceResendingToken = AtomicReference<PhoneAuthProvider.ForceResendingToken?>(null)

    val authState: Flow<AuthUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toAuthUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        trySend(firebaseAuth.currentUser?.toAuthUser())
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    fun currentUser(): AuthUser? = firebaseAuth.currentUser?.toAuthUser()

    fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    suspend fun sendOtp(phoneNumber: String, resend: Boolean = false): OtpSession =
        suspendCancellableCoroutine { cont ->
            val activity = try {
                activityHolder.require()
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to get Activity context for Phone Auth", t)
                cont.resumeWithException(AppError.FirebaseFailure("Unable to start verification. Activity context missing.", t))
                return@suspendCancellableCoroutine
            }

            Log.d(TAG, "Initiating phone verification (resend=$resend)")

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "Instant verification completed automatically")
                    if (cont.isActive) {
                        firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (!cont.isActive) return@addOnCompleteListener
                                if (task.isSuccessful) {
                                    Log.d(TAG, "Sign in with instant credential successful!")
                                    cont.resume(
                                        OtpSession(
                                            verificationId = AUTO_VERIFIED,
                                            phoneNumber = phoneNumber,
                                        ),
                                    )
                                } else {
                                    val exception = task.exception ?: AppError.FirebaseFailure()
                                    Log.e(TAG, "Sign in with instant credential failed: ${exception.message}", exception)
                                    cont.resumeWithException(exception)
                                }
                            }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "Phone verification failed. Code: ${e.message}", e)
                    if (cont.isActive) {
                        cont.resumeWithException(e)
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    Log.d(TAG, "SMS code sent successfully! Verification ID: $verificationId")
                    forceResendingToken.set(token)
                    if (cont.isActive) {
                        cont.resume(
                            OtpSession(
                                verificationId = verificationId,
                                phoneNumber = phoneNumber,
                            ),
                        )
                    }
                }
            }

            val builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)

            if (resend) {
                forceResendingToken.get()?.let { builder.setForceResendingToken(it) }
            }

            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }

    suspend fun verifyOtp(verificationId: String, smsCode: String): AuthUser {
        Log.d(TAG, "Verifying OTP code for verification ID: $verificationId")
        if (verificationId == AUTO_VERIFIED) {
            return currentUser() ?: throw AppError.Unauthorized()
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        val result = firebaseAuth.signInWithCredential(credential).await()
        Log.d(TAG, "OTP verification successful for user: ${result.user?.uid}")
        return result.user?.toAuthUser()
            ?: throw AppError.FirebaseFailure("Sign-in succeeded but user profile is missing.")
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    private fun com.google.firebase.auth.FirebaseUser.toAuthUser() = AuthUser(
        uid = uid,
        phoneNumber = phoneNumber,
    )

    companion object {
        const val AUTO_VERIFIED = "auto_verified"
    }
}
