package com.miled.marvel.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.miled.marvel.R
import com.miled.marvel.core.theme.BackgroundColor
import com.miled.marvel.core.theme.TextColor

val Monteserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semi_bold, FontWeight.SemiBold),
)
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 31.sp,
        color = TextColor
    ),
    h2 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = TextColor
    ),
    h3 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        color = TextColor
    ),
    h4 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = TextColor
    ),
    h5 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        color = TextColor
    ),
    h6 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        color = BackgroundColor
    ),
    body1 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = TextColor
    ),
    body2 = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = TextColor.copy(alpha = 0.5f)
    ),
    button = TextStyle(
        fontFamily = Monteserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        color = BackgroundColor,
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

)