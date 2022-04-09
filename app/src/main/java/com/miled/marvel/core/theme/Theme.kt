package com.miled.marvel.core.theme


import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.miled.marvel.ui.theme.Typography

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    onBackground = TextColor,
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = LightGrey,
)

@Composable
fun MarvelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}