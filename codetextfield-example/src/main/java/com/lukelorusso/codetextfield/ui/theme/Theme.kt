package com.lukelorusso.codetextfield.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = MaterialTheme(
    colors = resolveColors(darkTheme),
    typography = resolveTypography(),
    shapes = resolveShapes(),
    content = content
)
