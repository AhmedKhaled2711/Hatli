package com.app.hatli.core.data.firebase

import android.app.Activity
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Phone Auth requires an Activity for reCAPTCHA / Play Integrity.
 * MainActivity binds itself here — keeps Activity out of domain layer.
 */
@Singleton
class AuthActivityHolder @Inject constructor() {
    @Volatile
    private var activityRef: WeakReference<Activity>? = null

    fun set(activity: Activity?) {
        activityRef = activity?.let { WeakReference(it) }
    }

    fun getOrNull(): Activity? = activityRef?.get()

    fun require(): Activity =
        getOrNull() ?: error("No Activity bound for phone authentication")
}
