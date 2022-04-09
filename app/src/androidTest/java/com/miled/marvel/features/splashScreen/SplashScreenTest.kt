package com.miled.marvel.features.splashScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miled.marvel.MainActivity
import com.miled.marvel.core.navigation.Navigation
import com.miled.marvel.core.theme.MarvelTheme
import com.miled.marvel.utils.Constants.SPLASH_SCREEN_DURATION
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splashScreen_displaysAndDisappears() = runBlocking<Unit> {
        composeTestRule.setContent {
            MarvelTheme {
                Navigation()
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Marvel Logo")
            .assertExists()
            .assertIsDisplayed()

        delay(SPLASH_SCREEN_DURATION)

        composeTestRule
            .onNodeWithText("Comics")
            .assertIsDisplayed()

    }
}