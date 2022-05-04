package com.lukelorusso.codetextfield.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.lukelorusso.codetextfield.R

@Composable
fun resolveColors(darkTheme: Boolean): Colors = if (darkTheme) {
    darkColors(
        primary = colorResource(id = R.color.purple_200),
        primaryVariant = colorResource(id = R.color.purple_700),
        secondary = colorResource(id = R.color.teal_200),
        onPrimary = colorResource(id = R.color.white),
        onSecondary = colorResource(id = R.color.black)
    )
} else {
    lightColors(
        primary = colorResource(id = R.color.purple_500),
        primaryVariant = colorResource(id = R.color.purple_700),
        secondary = colorResource(id = R.color.teal_200),
        onPrimary = colorResource(id = R.color.black),
        onSecondary = colorResource(id = R.color.white)
    )
}
