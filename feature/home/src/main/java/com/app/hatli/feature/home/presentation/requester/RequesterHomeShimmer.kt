package com.app.hatli.feature.home.presentation.requester

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
import androidx.compose.foundation.lazy.LazyRow
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
 * Production-grade skeleton shimmer layout for Requester (Client) Home screen.
 * Mirrors the exact layout structure, spacing, cards, and dimensions of [RequesterHomeContent].
 */
@Composable
fun RequesterHomeShimmer(
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
            // 1. Location Pill & Notifications Header
            item(key = "shimmer_requester_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(160.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(20.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                    )
                }
            }

            // 2. Hero Create Errand Banner Placeholder
            item(key = "shimmer_hero_banner") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = HatliElevation.sm,
                    shadowElevation = HatliElevation.sm,
                    border = BorderStroke(1.dp, extended.border),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(HatliSpacing.lg),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(16.dp),
                                shape = RoundedCornerShape(6.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(190.dp)
                                    .height(22.dp),
                                shape = RoundedCornerShape(6.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(14.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }

                        ShimmerBox(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                        )
                    }
                }
            }

            // 3. Quick Service Categories Carousel Placeholder
            item(key = "shimmer_quick_categories") {
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(18.dp),
                        shape = RoundedCornerShape(4.dp),
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                        contentPadding = PaddingValues(horizontal = 2.dp),
                        userScrollEnabled = false,
                    ) {
                        items(4) {
                            Surface(
                                modifier = Modifier
                                    .width(148.dp)
                                    .height(138.dp),
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = HatliElevation.sm,
                                shadowElevation = HatliElevation.sm,
                                border = BorderStroke(1.dp, extended.border),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(HatliSpacing.md),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        ShimmerBox(
                                            modifier = Modifier.size(44.dp),
                                            shape = RoundedCornerShape(14.dp),
                                        )
                                        ShimmerBox(
                                            modifier = Modifier.size(16.dp),
                                            shape = CircleShape,
                                        )
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        ShimmerBox(
                                            modifier = Modifier
                                                .width(75.dp)
                                                .height(14.dp),
                                            shape = RoundedCornerShape(4.dp),
                                        )
                                        ShimmerBox(
                                            modifier = Modifier
                                                .width(105.dp)
                                                .height(11.dp),
                                            shape = RoundedCornerShape(4.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Current / Active Request Card Placeholder
            item(key = "shimmer_active_request") {
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(110.dp)
                            .height(18.dp),
                        shape = RoundedCornerShape(4.dp),
                    )

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
                            // Top Row: Category/Status Pill + Time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(88.dp)
                                        .height(24.dp),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(14.dp),
                                    shape = RoundedCornerShape(4.dp),
                                )
                            }

                            // Stepper Row: 4 dots + connection lines
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                repeat(4) { index ->
                                    ShimmerBox(
                                        modifier = Modifier.size(18.dp),
                                        shape = CircleShape,
                                    )
                                    if (index < 3) {
                                        ShimmerBox(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 6.dp)
                                                .height(3.dp),
                                            shape = RoundedCornerShape(2.dp),
                                        )
                                    }
                                }
                            }

                            // Bottom Row: Avatar + Title & Fee + Action Button
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
                                        modifier = Modifier.size(40.dp),
                                        shape = CircleShape,
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        ShimmerBox(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(14.dp),
                                            shape = RoundedCornerShape(4.dp),
                                        )
                                        ShimmerBox(
                                            modifier = Modifier
                                                .width(60.dp)
                                                .height(12.dp),
                                            shape = RoundedCornerShape(4.dp),
                                        )
                                    }
                                }

                                ShimmerBox(
                                    modifier = Modifier
                                        .width(84.dp)
                                        .height(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                )
                            }
                        }
                    }
                }
            }

            // 5. Recent Requests Section Placeholder
            item(key = "shimmer_recent_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(110.dp)
                            .height(18.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(55.dp)
                            .height(14.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                }
            }

            items(2) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = HatliElevation.sm,
                    shadowElevation = HatliElevation.sm,
                    border = BorderStroke(1.dp, extended.border),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(HatliSpacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ShimmerBox(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                        )
                        Spacer(modifier = Modifier.width(HatliSpacing.md))
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(14.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(85.dp)
                                    .height(12.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(20.dp),
                                shape = RoundedCornerShape(10.dp),
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(14.dp),
                                shape = RoundedCornerShape(4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
