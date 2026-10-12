package com.app.hatli.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.feature.profile.R

@Composable
fun ProfileHeader(
    fullName: String,
    selfieUrl: String?,
    isVerified: Boolean,
    showBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = LocalHatliExtendedColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        // Gradient Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            extendedColors.headerBackground,
                            extendedColors.headerBackground.copy(alpha = 0.88f)
                        )
                    )
                )
        ) {
            if (showBack) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(HatliSpacing.sm)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.profile_back_content_description),
                        tint = Color.White
                    )
                }
            }
        }

        // Avatar & Full Name
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (!selfieUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = selfieUrl,
                            contentDescription = stringResource(R.string.profile_photo_content_description),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = stringResource(R.string.profile_verified_badge_description),
                        tint = extendedColors.success,
                        modifier = Modifier
                            .size(26.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(HatliSpacing.xs))

            Text(
                text = fullName.ifBlank { stringResource(R.string.profile_default_user_name) },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}