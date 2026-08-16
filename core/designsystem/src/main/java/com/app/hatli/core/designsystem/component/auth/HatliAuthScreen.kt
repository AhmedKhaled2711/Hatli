package com.app.hatli.core.designsystem.component.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.theme.HatliSpacing


@Composable
fun HatliAuthScreen(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Spacer(
            modifier = Modifier.height(HatliSpacing.sm),
        )

        HatliAuthTopBar(
            title = title,
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HatliSpacing.lg),
            content = content,
        )
    }
}