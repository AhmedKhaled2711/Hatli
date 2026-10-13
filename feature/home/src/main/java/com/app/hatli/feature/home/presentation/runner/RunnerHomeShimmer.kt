package com.app.hatli.feature.home.presentation.runner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.home.presentation.components.shimmer.ShimmerCardContainer
import com.app.hatli.feature.home.presentation.components.shimmer.ShimmerCircle
import com.app.hatli.feature.home.presentation.components.shimmer.ShimmerHeaderRow
import com.app.hatli.feature.home.presentation.components.shimmer.ShimmerLine
import com.app.hatli.feature.home.presentation.components.shimmer.ShimmerSectionHeader

/**
 * Production-grade skeleton shimmer layout for Runner (Delivery) Home screen.
 * Mirrors the exact operational dashboard structure, stats row, active job card,
 * and nearby jobs list of [RunnerHomeContent].
 */
@Composable
fun RunnerHomeShimmer(
    modifier: Modifier = Modifier,
) {
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
                ShimmerHeaderRow(pillWidth = 136.dp, pillHeight = 38.dp)
            }

            // 2. Daily Operational Stats Row (Today Earnings, Jobs, Rating)
            item(key = "shimmer_stats_row") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                ) {
                    // Card 1: Today Earnings (weight 1.25f)
                    ShimmerCardContainer(
                        modifier = Modifier.weight(1.25f),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerLine(width = 45.dp, height = 12.dp)
                            ShimmerLine(width = 72.dp, height = 22.dp)
                        }
                    }

                    // Card 2: Today Jobs (weight 1f)
                    ShimmerCardContainer(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerLine(width = 40.dp, height = 12.dp)
                            ShimmerLine(width = 36.dp, height = 22.dp)
                        }
                    }

                    // Card 3: Rating (weight 1f)
                    ShimmerCardContainer(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .height(86.dp)
                                .padding(HatliSpacing.sm),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ShimmerLine(width = 40.dp, height = 12.dp)
                            ShimmerLine(width = 36.dp, height = 22.dp)
                        }
                    }
                }
            }

            // 3. Active Job Card Placeholder
            item(key = "shimmer_active_job") {
                ShimmerCardContainer(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
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
                                ShimmerCircle(size = 42.dp)
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    ShimmerLine(width = 110.dp, height = 15.dp)
                                    ShimmerLine(width = 70.dp, height = 12.dp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ShimmerCircle(size = 36.dp)
                                ShimmerCircle(size = 36.dp)
                            }
                        }

                        // Route pickup / dropoff placeholders
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                ShimmerCircle(size = 10.dp)
                                ShimmerLine(width = 190.dp, height = 13.dp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                ShimmerCircle(size = 10.dp)
                                ShimmerLine(width = 160.dp, height = 13.dp)
                            }
                        }

                        // Big Action Button placeholder
                        ShimmerLine(
                            modifier = Modifier.fillMaxWidth(),
                            height = 46.dp,
                            cornerRadius = 14.dp,
                        )
                    }
                }
            }

            // 4. Available / Nearby Jobs Section Placeholder
            item(key = "shimmer_nearby_header") {
                ShimmerSectionHeader(titleWidth = 160.dp, actionWidth = 60.dp)
            }

            items(2) {
                ShimmerCardContainer(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
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
                                ShimmerLine(width = 60.dp, height = 22.dp, cornerRadius = 8.dp)
                                ShimmerLine(width = 75.dp, height = 22.dp, cornerRadius = 8.dp)
                            }
                            ShimmerLine(width = 55.dp, height = 20.dp, cornerRadius = 8.dp)
                        }

                        // Addresses lines
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ShimmerLine(
                                modifier = Modifier.fillMaxWidth(0.85f),
                                height = 14.dp,
                            )
                            ShimmerLine(
                                modifier = Modifier.fillMaxWidth(0.65f),
                                height = 14.dp,
                            )
                        }

                        // Accept job button
                        ShimmerLine(
                            modifier = Modifier.fillMaxWidth(),
                            height = 40.dp,
                            cornerRadius = 12.dp,
                        )
                    }
                }
            }
        }
    }
}
