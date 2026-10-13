package com.app.hatli.feature.home.presentation.runner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.ShimmerBox
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Production-grade skeleton shimmer layout for Runner (Delivery) Home screen.
 * Mirrors the exact operational dashboard structure, stats row, active job card,
 * and nearby jobs list of [RunnerHomeContent].
 */
@Composable
fun RunnerHomeShimmer(
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = HatliSpacing.lg,
                end = HatliSpacing.lg,
                top = HatliSpacing.md,
                bottom = 128.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
            userScrollEnabled = false,
        ) {
            // 1. Availability Toggle & Notifications Header Row
            item(key = "shimmer_runner_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(136.dp)
                            .height(38.dp),
                        shape = RoundedCornerShape(20.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                    )
                }
            }

            // 2. Daily Operational Stats Row (Today Earnings, Jobs, Rating)
            item(key = "shimmer_stats_row") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                ) {
                    // Card 1: Today Earnings (weight 1.25f)
                    Surface(
                        modifier = Modifier.weight(1.25f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = HatliElevation.sm,
                        shadowElevation = HatliElevation.sm,
                        border = BorderStroke(1.dp, extended.border),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(12.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(72.dp)
                                    .height(22.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }
                    }

                    // Card 2: Today Jobs (weight 1f)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = HatliElevation.sm,
                        shadowElevation = HatliElevation.sm,
                        border = BorderStroke(1.dp, extended.border),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(12.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(22.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }
                    }

                    // Card 3: Rating (weight 1f)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = HatliElevation.sm,
                        shadowElevation = HatliElevation.sm,
                        border = BorderStroke(1.dp, extended.border),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(12.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(22.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }
                    }
                }
            }

            // 3. Active Job Card Placeholder
            item(key = "shimmer_active_job") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = HatliElevation.sm,
                    shadowElevation = HatliElevation.sm,
                    border = BorderStroke(1.dp, extended.border),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(HatliSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                    ) {
                        // Customer header row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                            ) {
                                ShimmerBox(
                                    modifier = Modifier.size(42.dp),
                                    shape = CircleShape,
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    ShimmerBox(
                                        modifier = Modifier
                                            .width(110.dp)
                                            .height(15.dp),
                                        shape = RoundedCornerShape(4.dp),
                                    )
                                    ShimmerBox(
                                        modifier = Modifier
                                            .width(70.dp)
                                            .height(12.dp),
                                        shape = RoundedCornerShape(4.dp),
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ShimmerBox(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                )
                                ShimmerBox(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                )
                            }
                        }

                        // Route pickup / dropoff placeholders
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                ShimmerBox(
                                    modifier = Modifier.size(10.dp),
                                    shape = CircleShape,
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(190.dp)
                                        .height(13.dp),
                                    shape = RoundedCornerShape(4.dp),
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                ShimmerBox(
                                    modifier = Modifier.size(10.dp),
                                    shape = CircleShape,
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(13.dp),
                                    shape = RoundedCornerShape(4.dp),
                                )
                            }
                        }

                        // Big Action Button placeholder
                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                        )
                    }
                }
            }

            // 4. Available / Nearby Jobs Section Placeholder
            item(key = "shimmer_nearby_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(160.dp)
                            .height(18.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                }
            }

            items(2) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = HatliElevation.sm,
                    shadowElevation = HatliElevation.sm,
                    border = BorderStroke(1.dp, extended.border),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(HatliSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // Badges Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(22.dp),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(75.dp)
                                        .height(22.dp),
                                    shape = RoundedCornerShape(8.dp),
                                )
                            }
                            ShimmerBox(
                                modifier = Modifier
                                    .width(55.dp)
                                    .height(20.dp),
                                shape = RoundedCornerShape(8.dp),
                            )
                        }

                        // Addresses lines
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .height(14.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(14.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }

                        // Accept job button
                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            shape = RoundedCornerShape(12.dp),
                        )
                    }
                }
            }
        }
    }
}
