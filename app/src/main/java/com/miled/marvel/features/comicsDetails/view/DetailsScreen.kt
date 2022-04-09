package com.miled.marvel.features.comicsDetails.view

import ComicsImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miled.marvel.R
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.Item
import com.miled.marvel.features.comics.viewModel.ComicsViewModelState
import com.miled.marvel.features.favoriteComics.viewModel.FavoriteComicsViewModel
import org.kodein.di.compose.rememberInstance

@Composable
fun DetailsScreen(comic: Comic) {
    val favoriteComicsViewModel: FavoriteComicsViewModel by rememberInstance()
    val isFavorite = favoriteComicsViewModel.isComicFavorite
    val favoriteComicsViewModelState = favoriteComicsViewModel.comicsViewModelState
    LaunchedEffect(Unit) {
        favoriteComicsViewModel.comicIsFavorite(comicId = comic.id)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)) {
            Card {
                Column {
                    ComicsImage(
                        imagePath = comic.thumbnail?.coverImagePath ?: "",
                        Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                    )
                    ComicTitleWithIcon(
                        isFavorite = isFavorite,
                        comic = comic,
                        comicsViewModelState = favoriteComicsViewModelState,
                        addToFavorite = { favoriteComicsViewModel.addFavoriteComics(it) },
                        removeFromFavorite = { favoriteComicsViewModel.deleteFavoriteComic(it) },
                    )
                    if (comic.description != null)
                        Text(
                            text = comic.description,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    ComicFields(comic = comic)
                }
            }
            if (comic.characters?.characterList?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(20.dp))
                CharactersOrCreatorFields(
                    list = comic.characters.characterList,
                    title = stringResource(id = R.string.favorite_comics_characters)
                )
            }
            if (comic.creators?.creatorsList?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(20.dp))
                CharactersOrCreatorFields(
                    list = comic.creators.creatorsList,
                    title = stringResource(id = R.string.favorite_comics_creators)
                )
            }
        }
    }
}

@Composable
private fun ComicTitleWithIcon(
    isFavorite: State<Boolean>,
    comic: Comic,
    comicsViewModelState: State<ComicsViewModelState>,
    removeFromFavorite: (Int) -> Unit,
    addToFavorite: (Comic) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = comic.title ?: "",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.weight(6f)
        )
        if (isFavorite.value)
            IconButton(onClick = {
                removeFromFavorite(
                    comic.id ?: 0
                )
            }) {
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = stringResource(id = R.string.comics_screen_favorite_icon_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)

                )
            }
        else
            IconButton(onClick = {
                addToFavorite(
                    comic
                )
            }) {
                Icon(
                    if (comicsViewModelState.value is ComicsViewModelState.OnError) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                    contentDescription = stringResource(id = R.string.comics_screen_favorite_icon_description),
                    tint = if (comicsViewModelState.value is ComicsViewModelState.OnError) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                )
            }
    }
}


@Composable
private fun ComicFields(comic: Comic) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        ComicsField(stringResource(id = R.string.favorite_comics_format), comic.format ?: "")
        ComicsField(
            stringResource(id = R.string.favorite_comics_price),
            comic.comicPrice
        )
        ComicsField(stringResource(id = R.string.favorite_comics_pages), comic.safePageCount)
    }
}

@Composable
private fun CharactersOrCreatorFields(list: List<Item>, title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    count = list.size,
                    itemContent = { index ->
                        ComicsField(
                            list[index].role ?: "",
                            list[index].name ?: ""
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ComicsField(name: String, value: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        if (name.isNotEmpty())
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp)
            )
        Text(
            text = value,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}