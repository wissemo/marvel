import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.miled.marvel.R
import com.miled.marvel.composables.ComicsGridListComposable
import com.miled.marvel.features.favoriteComics.viewModel.FavoriteComicsViewModel
import org.kodein.di.compose.rememberInstance

@Composable
fun FavoriteScreen(navController: NavController) {
    val favoriteComicsViewModel: FavoriteComicsViewModel by rememberInstance()
    val favoriteComicsList = favoriteComicsViewModel.favoriteComicsList.observeAsState()
    val favoriteComicsViewModelState = favoriteComicsViewModel.comicsViewModelState
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.favorite_comics_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        favoriteComicsViewModel.deleteAllFavoriteComics()
                    }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.favorite_comics_delete_icon_description)
                        )
                    }
                }

            )
        },
    ) {
        ComicsGridListComposable(
            navController = navController,
            listComics = favoriteComicsList.value ?: emptyList(),
            comicsViewModelState = favoriteComicsViewModelState.value,
            scrollState = scrollState,
        )
    }
}