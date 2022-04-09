package com.miled.marvel.core.navigation

import FavoriteScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.view.ComicsScreen
import com.miled.marvel.features.comicsDetails.view.DetailsScreen
import com.miled.marvel.features.splashScreen.SplashScreen
import com.miled.marvel.utils.Constants.COMIC_KEY

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.ComicsScreen.route) {
            ComicsScreen(navController = navController)
        }
        composable(Screen.ComicDetailScreen.route) {
            navController.previousBackStackEntry?.savedStateHandle?.get<Comic>(COMIC_KEY)?.let {
                DetailsScreen(comic = it)
            }
        }
        composable(Screen.FavoriteScreen.route) {
            FavoriteScreen(navController = navController)
        }
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
    }
}