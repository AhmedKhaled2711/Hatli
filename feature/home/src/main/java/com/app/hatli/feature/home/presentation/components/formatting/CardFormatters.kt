package com.app.hatli.feature.home.presentation.components.formatting

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.app.hatli.feature.home.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun isAppArabic(): Boolean =
    LocalLayoutDirection.current == LayoutDirection.Rtl || Locale.getDefault().language == "ar"

@Composable
fun formatRelativeTime(epochMs: Long, isAr: Boolean = false): String {
    if (epochMs <= 0L) return ""
    val diffMs = System.currentTimeMillis() - epochMs
    if (diffMs < 0L) return stringResource(R.string.home_card_time_just_now)
    val minutes = diffMs / 60_000L
    val hours = diffMs / 3_600_000L
    val days = diffMs / 86_400_000L
    return when {
        minutes < 1 -> stringResource(R.string.home_card_time_just_now)
        minutes < 60 -> stringResource(R.string.home_card_time_mins_ago, minutes.toInt())
        hours < 24 -> stringResource(R.string.home_card_time_hours_ago, hours.toInt())
        days < 30 -> stringResource(R.string.home_card_time_days_ago, days.toInt())
        else -> stringResource(R.string.home_card_time_more_than_month)
    }
}

@Composable
fun formatEgp(amount: Double?, isAr: Boolean = false): String {
    if (amount == null) return ""
    val formatted = NumberFormat.getIntegerInstance(Locale.US).format(amount.toInt())
    return "$formatted ${stringResource(R.string.home_card_currency_egp)}"
}

@Composable
fun formatDistance(meters: Int?, isAr: Boolean = false): String {
    if (meters == null) return ""
    return if (meters >= 1000) {
        val km = String.format(Locale.US, "%.1f", meters / 1000.0)
        stringResource(R.string.home_card_distance_km, km)
    } else {
        stringResource(R.string.home_card_distance_m, meters)
    }
}
