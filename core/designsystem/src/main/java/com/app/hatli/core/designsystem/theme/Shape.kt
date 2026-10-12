package com.app.hatli.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

val HatliShapes = Shapes(
    extraSmall = RoundedCornerShape(HatliRadius.chip),
    small = RoundedCornerShape(HatliRadius.sm),
    medium = RoundedCornerShape(HatliRadius.button),
    large = RoundedCornerShape(HatliRadius.card),
    extraLarge = RoundedCornerShape(HatliRadius.modal),
)
