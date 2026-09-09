package com.app.hatli.feature.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors

@Composable
fun SendMessageButton(
    enabled: Boolean,
    isSending: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape),
        shape = CircleShape,
        color = if (enabled) {
            HatliColors.BrandTeal
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        onClick = onClick,
        enabled = enabled,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            AnimatedVisibility(
                visible = isSending,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = HatliColors.DeepTeal,
                )
            }

            AnimatedVisibility(
                visible = !isSending,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send message",
                    tint = if (enabled) {
                        HatliColors.DeepTeal
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}