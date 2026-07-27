package com.app.hatli.core.data.auth

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
                cont.resumeWithException(AppError.FirebaseFailure("Unable to start verification.", t))
                return@suspendCancellableCoroutine
            }

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval / instant verification — sign in immediately if still waiting.
                    if (cont.isActive) {
                        firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (!cont.isActive) return@addOnCompleteListener
                                if (task.isSuccessful) {
                                    cont.resume(
                                        OtpSession(
                                            verificationId = AUTO_VERIFIED,
                                            phoneNumber = phoneNumber,
                                        ),
                                    )
                                } else {
                                    cont.resumeWithException(
                                        task.exception ?: AppError.FirebaseFailure(),
                                    )
                                }
                            }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (cont.isActive) cont.resumeWithException(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
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
        if (verificationId == AUTO_VERIFIED) {
            return currentUser() ?: throw AppError.Unauthorized()
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user?.toAuthUser()
            ?: throw AppError.FirebaseFailure("Sign-in succeeded but user is missing.")
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
