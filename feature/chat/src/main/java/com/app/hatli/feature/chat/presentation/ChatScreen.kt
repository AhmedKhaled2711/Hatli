package com.app.hatli.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.designsystem.theme.TealContainer
import com.app.hatli.feature.chat.components.ChatComposer
import com.app.hatli.feature.chat.components.ChatEmptyState
import com.app.hatli.feature.chat.components.ChatMessageBubble

@Composable
fun ChatScreen(
    state: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteCanvas),
    ) {
        // Custom Counterpart Header Bar
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HatliSpacing.sm, vertical = HatliSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CharcoalDark
                        )
                    }

                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = BrandTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        // Online green dot indicator
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(BrandTeal)
                        )
                    }

                    Spacer(modifier = Modifier.width(HatliSpacing.sm))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "محادثة التوصيل المباشرة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalDark
                        )
                        Text(
                            text = "نشط الآن • كابتن التوصيل",
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandTeal
                        )
                    }
                }

                // Sticky Order Summary Strip Banner
                Surface(
                    color = HatliColors.CoralContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = HatliSpacing.md, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = BrandCoral,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "متابعة حالة الطلب الحالي",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = BrandCoral
                            )
                        }
                        Text(
                            text = "في الطريق 🛵",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalDark
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (state.messages.isEmpty()) {
                ChatEmptyState(
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = HatliSpacing.md,
                        vertical = HatliSpacing.md,
                    ),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    items(
                        items = state.messages,
                        key = { it.id },
                    ) { message ->
                        ChatMessageBubble(
                            text = message.text,
                            isMine = message.isMine,
                            senderLabel = message.senderLabel,
                        )
                    }
                }
            }
        }

        ChatComposer(
            value = state.draft,
            isSending = state.isSending,
            onValueChange = {
                onEvent(ChatUiEvent.DraftChanged(it))
            },
            onSend = {
                onEvent(ChatUiEvent.Send)
            },
        )
    }
}
