package com.app.hatli.core.ui.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Safely extracts the enclosing Activity from any Context/ContextWrapper in Compose UI.
 */
fun Context.findActivity(): Activity? {
    var current = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
