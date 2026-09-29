package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.designsystem.theme.HatliThemeMode
import com.app.hatli.feature.home.R

@Composable
fun HomeHeader(
    userName: String,
    unreadNotifications: Int,
    isAr: Boolean,
    completedRequests: Int,
    totalRequests: Int,
    onNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 32.dp,
            ),
            color = HatliTheme.extendedColors.headerBackground,
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(
                        start = HatliSpacing.lg,
                        end = HatliSpacing.lg,
                        top = HatliSpacing.sm,
                        bottom = HatliSpacing.sm,
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    HatliSpacing.xl,
                ),
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        HatliSpacing.md,
                    ),
                ) {

                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = stringResource(
                                    R.string.home_default_profile_image,
                                ),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(
                            HatliSpacing.xs,
                        ),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.home_good_morning,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HatliTheme.extendedColors.onHeaderBackground,                        )

                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = HatliTheme.extendedColors.onHeaderBackground,                        )
                    }

                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge {
                                    Text(
                                        text = if (unreadNotifications > 99) {
                                            "99+"
                                        } else {
                                            unreadNotifications.toString()
                                        },
                                    )
                                }
                            }
                        },
                    ) {
                        IconButton(
                            onClick = onNotifications,
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape,
                                ),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = stringResource(
                                    R.string.home_notifications,
                                ),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }

    }
}

@Preview(
    name = "Home Header - Light",
    showBackground = true,
    showSystemUi = true,
    widthDp = 360,
)
@Composable
private fun HomeHeaderLightPreview() {
    HatliTheme(
        themeMode = HatliThemeMode.Light,
    ) {
        HomeHeader(
            userName = "Ahmed",
            unreadNotifications = 3,
            isAr = false,
            completedRequests = 500,
            totalRequests = 500,
            onNotifications = {},
        )
    }
}

@Preview(
    name = "Home Header - Dark",
    showBackground = true,
    showSystemUi = true,
    widthDp = 360,
)
@Composable
private fun HomeHeaderDarkPreview() {
    HatliTheme(
        themeMode = HatliThemeMode.Dark,
    ) {
        HomeHeader(
            userName = "Ahmed",
            unreadNotifications = 3,
            isAr = false,
            completedRequests = 500,
            totalRequests = 500,
            onNotifications = {},
        )
    }
}