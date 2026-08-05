package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    unread: Int,
    onNotifications: () -> Unit,
    showNotifications: Boolean = false,
) {
    // Owns status-bar insets once (root Scaffold no longer pads). Compact 56dp chrome.
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        actions = {
            if (showNotifications) {
                IconButton(onClick = onNotifications) {
                    BadgedBox(
                        badge = {
                            if (unread > 0) {
                                Badge { Text("$unread") }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                        )
                    }
                }
            }
        },
        expandedHeight = 56.dp,
        windowInsets = TopAppBarDefaults.windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Composable
fun HeroCreateCard(
    title: String,
    cta: String,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            HatliPrimaryButton(text = cta, onClick = onCreate)
        }
    }
}

@Composable
fun CurrentRequestCard(
    summary: RequestSummary,
    modifier: Modifier = Modifier,
) {
    val statusLabel = when (summary.status) {
        RequestStatus.SearchingRunner -> "جاري البحث عن Runner"
        RequestStatus.Accepted -> "${summary.runnerName ?: "Runner"} وافق على طلبك"
        RequestStatus.InProgress -> "الطلب قيد التنفيذ"
        RequestStatus.Completed -> "تم التوصيل"
        else -> summary.status.name
    }
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text(statusLabel, style = MaterialTheme.typography.titleMedium)
            Text(summary.title, style = MaterialTheme.typography.bodyLarge)
            summary.etaMinutes?.let {
                Text("$it دقيقة", style = MaterialTheme.typography.bodyMedium)
            }
            if (summary.status == RequestStatus.Accepted || summary.status == RequestStatus.InProgress) {
                Text("تابع الطلب", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun RequestRowCard(
    summary: RequestSummary,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
            Text(summary.title, style = MaterialTheme.typography.titleMedium)
            Text(summary.status.name, style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                summary.distanceMeters?.let {
                    Text("$it م", style = MaterialTheme.typography.bodyMedium)
                }
                summary.offerAmount?.let {
                    Text("${it.toInt()} جنيه", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun RunnerPresenceCard(
    isOnline: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
            Text(
                text = if (isOnline) "🟢 Online" else "🔴 Offline",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            if (isOnline) {
                HatliSecondaryButton(text = "Go Offline", onClick = onToggle)
            } else {
                HatliPrimaryButton(text = "Go Online", onClick = onToggle)
            }
        }
    }
}

@Composable
fun RunnerStatsRow(
    earnings: Double,
    jobs: Int,
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        StatChip(
            modifier = Modifier.weight(1f),
            title = "${earnings.toInt()} جنيه",
            subtitle = "اليوم",
        )
        StatChip(
            modifier = Modifier.weight(1f),
            title = "$jobs",
            subtitle = "طلبات",
        )
        StatChip(
            modifier = Modifier.weight(1f),
            title = "$rating ⭐",
            subtitle = "تقييم",
        )
    }
}

@Composable
private fun StatChip(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = HatliSpacing.xs),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
