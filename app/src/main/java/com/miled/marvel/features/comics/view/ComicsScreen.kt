package com.miled.marvel.features.comics.view


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miled.marvel.R
import com.miled.marvel.composables.ComicsGridListComposable
import com.miled.marvel.composables.TextFieldComposable
import com.miled.marvel.core.navigation.Navigation
import com.miled.marvel.core.navigation.Screen
import com.miled.marvel.core.theme.MarvelTheme
import com.miled.marvel.features.comics.viewModel.ComicsViewModel
import gridReachedEnd
import org.kodein.di.compose.rememberInstance

@Composable
fun ComicsScreen(navController: NavController) {
    val comicsViewModel: ComicsViewModel by rememberInstance()
    val comicsList = comicsViewModel.comicsList.observeAsState()
    val comicsViewModelState = comicsViewModel.comicsViewModelState
    val scrollState = rememberLazyListState()

    if (scrollState.isScrollInProgress && gridReachedEnd(
            firstVisibleIndex = scrollState.firstVisibleItemIndex,
            screenMaxDisplay = scrollState.layoutInfo.visibleItemsInfo.size,
            listComicsSize = comicsViewModel.loadedItems
        )
    )
        comicsViewModel.loadMoreComics()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.comics_screen_title)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.FavoriteScreen.route) }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = stringResource(id = R.string.comics_screen_favorite_icon_description)
                        )
                    }
                },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                TextFieldComposable(
                    hint = stringResource(id = R.string.comics_screen_edit_text_hint),
                    onClearText = { comicsViewModel.clearSearchTitle() },
                    text = comicsViewModel.searchByTitle.value,
                    onValueChange = { comicsViewModel.setSearchTitle(it) },
                    onSearch = { comicsViewModel.searchByTitle() }
                )
                ComicsGridListComposable(
                    navController = navController,
                    listComics = comicsList.value ?: emptyList(),
                    comicsViewModelState = comicsViewModelState.value,
                    scrollState = scrollState,
                )
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarvelTheme {
        Navigation()
    }
}