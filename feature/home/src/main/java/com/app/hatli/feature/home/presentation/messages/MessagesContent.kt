package com.app.hatli.feature.home.presentation.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.card.ConversationRowCard

@Composable
fun MessagesContent(
    conversations: List<RequestSummary>,
    isAr: Boolean,
    onOpenChat: (String) -> Unit,
) {
    if (conversations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(HatliSpacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    HatliSpacing.sm,
                ),
            ) {
                Text(
                    text = stringResource(
                        R.string.home_messages_empty_title,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = stringResource(
                        R.string.home_messages_empty_description,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = HatliSpacing.md,
                end = HatliSpacing.md,
                top = HatliSpacing.md,
                bottom = 128.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(
                HatliSpacing.sm,
            ),
        ) {
            items(
                items = conversations,
                key = { it.id },
            ) { conversation ->
                ConversationRowCard(
                    summary = conversation,
                    isAr = isAr,
                    onClick = {
                        onOpenChat(conversation.id)
                    },
                )
            }
        }
    }
}