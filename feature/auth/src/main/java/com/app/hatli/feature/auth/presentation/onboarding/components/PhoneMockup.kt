package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing

/**
 * Phone bezel sized like the FlexBox reference — tall enough that the sheet
 * covers the lower third when overlaid.
 */
@Composable
fun PhoneMockup(
    modifier: Modifier = Modifier,
    screenContent: @Composable BoxScope.() -> Unit,
) {
    val bezel = RoundedCornerShape(HatliRadius.xl + 8.dp)
    val screen = RoundedCornerShape(HatliRadius.xl)

    Box(
        modifier = modifier
            .fillMaxWidth(0.58f)
            .aspectRatio(9f / 19.5f)
            .shadow(
                elevation = HatliElevation.lg,
                shape = bezel,
                clip = false,
            )
            .clip(bezel)
            .background(Color.White)
            .padding(all = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(0.34f)
                .fillMaxHeight(0.028f)
                .clip(RoundedCornerShape(percent = 50))
                .background(Color.Black.copy(alpha = 0.55f)),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = HatliSpacing.sm)
                .clip(screen)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                    shape = screen,
                ),
            content = screenContent,
        )
    }
}
