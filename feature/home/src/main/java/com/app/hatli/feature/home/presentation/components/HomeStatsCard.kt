package com.app.hatli.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.feature.home.R

data class HomeStatItem(
    val title: String,
    val value: Int,
)

@Composable
fun HomeStatsCard(
    firstStat: HomeStatItem,
    secondStat: HomeStatItem,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = HatliTheme.extendedColors.statsCardBackground,
        tonalElevation = 2.dp,
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HatliSpacing.sm,
                    vertical = HatliSpacing.xxs,
                ),
        ) {
            HomeStat(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                icon = R.drawable.completed_request,
                title = firstStat.title,
                value = firstStat.value,
            )

            HomeStat(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                icon = R.drawable.completed_request,
                title = secondStat.title,
                value = secondStat.value,
            )
        }
    }
}

@Composable
fun HomeStat(
    modifier: Modifier,
    @DrawableRes icon: Int,
    title: String,
    value: Int,
    horizontalAlignment: Alignment.Horizontal,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(
            HatliSpacing.sm,
        ),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = HatliTheme.extendedColors.onWhiteAndBlack,
        )

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displaySmall,
            color = HatliTheme.extendedColors.onStatsCardBackground,
        )
    }
}