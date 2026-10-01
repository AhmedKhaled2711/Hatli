package com.app.hatli.feature.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing

@Composable
fun ChatMessageBubble(
    text: String,
    isMine: Boolean,
    senderLabel: String,
) {
    val bubbleColor = if (isMine) {
        BrandCoral
    } else {
        Color.White
    }

    val contentColor = if (isMine) {
        Color.White
    } else {
        CharcoalDark
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(fraction = 0.82f),
            shape = if (isMine) {
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 4.dp,
                )
            } else {
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 20.dp,
                )
            },
            color = bubbleColor,
            shadowElevation = if (isMine) 1.dp else 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = HatliSpacing.md,
                    vertical = 10.dp,
                ),
            ) {
                if (!isMine && senderLabel.isNotBlank()) {
                    Text(
                        text = senderLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.Bold,
                        color = BrandTeal,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = contentColor,
                    lineHeight = 20.sp
                )

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isMine) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
