package com.app.hatli.feature.home.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.feature.home.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RunnerStatsRow(
    earnings: Double,
    jobs: Int,
    rating: Double,
    isAr: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        val amountFormatted = NumberFormat.getIntegerInstance(Locale.US).format(earnings.toInt())
        val currencyText = stringResource(R.string.home_card_currency_egp)
        val ratingFormatted = if (rating > 0.0) {
            String.format(Locale.US, "%.1f", rating)
        } else {
            "—"
        }

        // Today Earnings Card (given 1.25f weight so financial value has sufficient breathing room)
        StatCardContainer(
            modifier = Modifier.weight(1.25f),
            label = stringResource(R.string.home_runner_stat_today),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    text = amountFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
                Text(
                    text = currencyText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }

        // Jobs Card
        StatCardContainer(
            modifier = Modifier.weight(0.85f),
            label = stringResource(R.string.home_runner_stat_jobs),
        ) {
            Text(
                text = jobs.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
        }

        // Rating Card
        StatCardContainer(
            modifier = Modifier.weight(0.9f),
            label = stringResource(R.string.home_runner_stat_rating),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    text = ratingFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AmberPending,
                    modifier = Modifier.size(13.dp),
                )
            }
        }
    }
}

@Composable
private fun StatCardContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val extended = HatliTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(3.dp))
            content()
        }
    }
}
