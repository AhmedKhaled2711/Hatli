package com.app.hatli.feature.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas

private val QUICK_REPLIES = listOf(
    "أنا في المحل حالياً 🛒",
    "الصنف ده ناقص ⚠️",
    "وصلت تحت البيت 📍",
    "تمام يا غالي 👍"
)

@Composable
fun ChatComposer(
    value: String,
    isSending: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.ime)
    ) {
        Column(
            modifier = Modifier.padding(bottom = HatliSpacing.xs)
        ) {
            // Quick reply carousel chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HatliSpacing.md, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(QUICK_REPLIES) { chipText ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = OffWhiteCanvas,
                        modifier = Modifier.clickable { onValueChange(chipText) }
                    ) {
                        Text(
                            text = chipText,
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                            color = CharcoalDark,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HatliSpacing.md, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Attachment / Camera Action
                IconButton(
                    onClick = { /* Photo attachment hook */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Attach image",
                        tint = CharcoalMuted
                    )
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = "اكتب رسالتك للكابتن...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CharcoalMuted
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandCoral,
                        unfocusedBorderColor = OffWhiteCanvas,
                        focusedContainerColor = OffWhiteCanvas,
                        unfocusedContainerColor = OffWhiteCanvas,
                    ),
                    maxLines = 3,
                )

                // Send Button with Coral Circular Fill
                Surface(
                    shape = CircleShape,
                    color = if (value.isNotBlank() && !isSending) BrandCoral else CoralContainer,
                    modifier = Modifier.size(46.dp)
                ) {
                    IconButton(
                        enabled = value.isNotBlank() && !isSending,
                        onClick = onSend,
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = BrandCoral,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (value.isNotBlank()) Color.White else CharcoalMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
