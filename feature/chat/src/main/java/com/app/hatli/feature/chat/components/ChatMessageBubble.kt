package com.app.hatli.feature.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing

@Composable
fun ChatMessageBubble(
    text: String,
    isMine: Boolean,
    senderLabel: String,
) {
    val bubbleColor = if (isMine) {
        HatliColors.BrandTeal
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isMine) {
        HatliColors.DeepTeal
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(
                fraction = 0.78f,
            ),
            shape = if (isMine) {
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 4.dp,
                )
            } else {
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 18.dp,
                )
            },
            color = bubbleColor,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = HatliSpacing.md,
                    vertical = HatliSpacing.sm,
                ),
            ) {
                Text(
                    text = senderLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor.copy(alpha = 0.75f),
                )

                Spacer(
                    modifier = Modifier.size(HatliSpacing.xxs),
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor,
                )
            }
        }
    }
}
