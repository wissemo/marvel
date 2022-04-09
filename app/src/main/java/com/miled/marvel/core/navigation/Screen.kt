package com.miled.marvel.core.navigation

sealed class Screen(val route: String) {
    object ComicsScreen : Screen("comics_screen")
    object SplashScreen : Screen("home_screen")
    object FavoriteScreen : Screen("favorite_screen")
    object ComicDetailScreen : Screen("details_screen")
}
