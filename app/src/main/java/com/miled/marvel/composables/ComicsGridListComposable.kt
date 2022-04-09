package com.miled.marvel.composables

import ComicsImage
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.miled.marvel.R
import com.miled.marvel.core.navigation.Screen
import com.miled.marvel.core.theme.transparentBackground
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.viewModel.ComicsViewModelState
import com.miled.marvel.utils.Constants.CELL_COUNT
import com.miled.marvel.utils.Constants.COMIC_KEY


@OptIn(ExperimentalFoundationApi::class)
private val span: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(CELL_COUNT) }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComicsGridListComposable(
    navController: NavController,
    listComics: List<Comic>,
    comicsViewModelState: ComicsViewModelState,
    scrollState: LazyListState
) {
    LazyVerticalGrid(
        state = scrollState,
        cells = GridCells.Fixed(CELL_COUNT),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(),
        content = {
            items(listComics.size) { index ->
                ComicItem(navController = navController, listComics[index])
            }
            renderLoading(comicsViewModelState = comicsViewModelState)
            renderError(comicsViewModelState = comicsViewModelState)
            renderNoMoreComics(comicsViewModelState = comicsViewModelState)
            renderNoComics(comicsViewModelState = comicsViewModelState, listComics.isEmpty())
        },
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComicItem(navController: NavController, comic: Comic) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .height(300.dp)
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp,
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set(COMIC_KEY, comic)
            navController.navigate(Screen.ComicDetailScreen.route)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ComicsImage(
                imagePath = comic.thumbnail?.coverImagePath ?: "",
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(transparentBackground)
            ) {
                ComicTitle(comic.title ?: "")
            }
        }

    }
}

@Composable
private fun ComicTitle(name: String) = Text(
    text = name,
    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
    style = MaterialTheme.typography.subtitle1.copy(
        color = Color.White,
        letterSpacing = 1.5.sp,
    ),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderLoading(comicsViewModelState: ComicsViewModelState) {
    if (comicsViewModelState is ComicsViewModelState.Loading)
        item(span = span) {
            LoadingRow(text = stringResource(id = R.string.comic_loading))
        }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderError(comicsViewModelState: ComicsViewModelState) {
    if (comicsViewModelState is ComicsViewModelState.OnError)
        item(span = span) {
            ErrorRow(text = stringResource(id = comicsViewModelState.messageId))
        }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderNoMoreComics(comicsViewModelState: ComicsViewModelState) {
    if (comicsViewModelState is ComicsViewModelState.AllLoaded)
        item(span = span) {
            NoMoreItems()
        }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderNoComics(
    comicsViewModelState: ComicsViewModelState,
    isEmpty: Boolean
) {
    if (comicsViewModelState is ComicsViewModelState.IsLoaded && isEmpty)
        item(span = span) {
            NoComicsFound()
        }
}
