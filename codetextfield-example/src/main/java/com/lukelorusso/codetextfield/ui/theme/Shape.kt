package com.lukelorusso.codetextfield.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

const val SMALL_ROUNDED_CORNER_RADIUS = 4
const val MEDIUM_ROUNDED_CORNER_RADIUS = 8
const val LARGE_ROUNDED_CORNER_RADIUS = 12

@Composable
fun resolveShapes(): Shapes = Shapes(
    small = RoundedCornerShape(SMALL_ROUNDED_CORNER_RADIUS.dp),
    medium = RoundedCornerShape(MEDIUM_ROUNDED_CORNER_RADIUS.dp),
    large = RoundedCornerShape(LARGE_ROUNDED_CORNER_RADIUS.dp)
)
