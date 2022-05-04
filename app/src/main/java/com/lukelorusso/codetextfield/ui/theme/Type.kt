package com.lukelorusso.codetextfield.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun resolveTypography(): Typography = Typography(
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ), // normal body
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ), // normal body, with specified line height
    button = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ), // text inside buttons
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ), // small hint, like minutes remaining, max file size, etc...
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        letterSpacing = 0.15.sp
    ), // header in pages, mostly used for titles near a page icon
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ), // small text, used mostly on explanations, with specified (large) line height
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ) // small text, used mostly on explanations, with specified letter spacing
)
