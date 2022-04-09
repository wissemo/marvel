package com.miled.marvel.features.favoriteComics.viewModel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.repository.ComicsRepository
import com.miled.marvel.features.comics.viewModel.ComicsViewModelState
import kotlinx.coroutines.launch

class FavoriteComicsViewModel(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    private val _favoriteComicsList = MutableLiveData<List<Comic>>(emptyList())
    val favoriteComicsList: LiveData<List<Comic>> = _favoriteComicsList

    private val _favoriteComicsViewModelState =
        mutableStateOf<ComicsViewModelState>(ComicsViewModelState.Initial)
    val comicsViewModelState: State<ComicsViewModelState> = _favoriteComicsViewModelState

    private val _isComicFavorite = mutableStateOf(false)
    val isComicFavorite: State<Boolean> = _isComicFavorite

    init {
        loadFavoriteComics()
    }

    @VisibleForTesting
    fun loadFavoriteComics() {
        viewModelScope.launch {
            comicsRepository.getCachedComics(
                onError = { messageId -> onFavoriteComicsFailed(messageId = messageId) },
                onSuccess = { result -> onFavoriteComicsLoaded(result) })
        }
    }

    internal fun deleteFavoriteComic(comicId: Int?) {
        comicId?.let {
            viewModelScope.launch {
                comicsRepository.deleteCachedComic(
                    comicId = comicId,
                    onError = { messageId -> onFavoriteComicsFailed(messageId = messageId) },
                    onSuccess = { result -> onFavoriteComicDelete(result) })
            }
        }
    }

    internal fun deleteAllFavoriteComics() {
        viewModelScope.launch {
            comicsRepository.deleteCachedComics(
                onError = { messageId -> onFavoriteComicsFailed(messageId = messageId) },
                onSuccess = { result -> onFavoriteComicsLoaded(result) })
        }
    }

    internal fun addFavoriteComics(comic: Comic) {
        viewModelScope.launch {
            comicsRepository.cacheComic(
                comic = comic,
                onError = { messageId -> onFavoriteComicsFailed(messageId = messageId) },
                onSuccess = { result -> onFavoriteComicAdded(result) })
        }
    }

    internal fun comicIsFavorite(comicId: Int?) {
        _isComicFavorite.value = _favoriteComicsList.value?.firstOrNull { it.id == comicId } != null
    }
    @VisibleForTesting
    fun onFavoriteComicsLoaded(favoriteComics: List<Comic>) {
        _favoriteComicsList.value = favoriteComics
        _favoriteComicsViewModelState.value = ComicsViewModelState.IsLoaded
    }

    @VisibleForTesting
    fun onFavoriteComicDelete(favoriteComics: List<Comic>) {
        _favoriteComicsList.value = favoriteComics
        _favoriteComicsViewModelState.value = ComicsViewModelState.IsLoaded
        _isComicFavorite.value = false
    }

    @VisibleForTesting
    fun onFavoriteComicAdded(favoriteComics: List<Comic>) {
        _favoriteComicsList.value = favoriteComics
        _favoriteComicsViewModelState.value = ComicsViewModelState.IsLoaded
        _isComicFavorite.value = true
    }

    @VisibleForTesting
    fun onFavoriteComicsFailed(messageId: Int) {
        _favoriteComicsViewModelState.value = ComicsViewModelState.OnError(messageId = messageId)
    }
}