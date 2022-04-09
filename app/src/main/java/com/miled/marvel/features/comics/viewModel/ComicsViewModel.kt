package com.miled.marvel.features.comics.viewModel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.features.comics.repository.ComicsRepository
import com.miled.marvel.utils.Constants.LOAD_SIZE
import kotlinx.coroutines.launch
import timber.log.Timber

class ComicsViewModel(
    private val comicsRepository: ComicsRepository
) : ViewModel() {


    @VisibleForTesting
    var totalCount: Int = 0
    @VisibleForTesting
    var offset: Int = 0
    var loadedItems: Int = 0
        private set
    private val _searchByTitle = mutableStateOf("")
    val searchByTitle: State<String> = _searchByTitle

    private val _comicsList = MutableLiveData<List<Comic>>(emptyList())
    val comicsList: LiveData<List<Comic>> = _comicsList

    private val _comicsViewModelState =
        mutableStateOf<ComicsViewModelState>(ComicsViewModelState.Initial)
    val comicsViewModelState: State<ComicsViewModelState> = _comicsViewModelState

    init {
        loadComics()
    }

    internal fun setSearchTitle(search: String) {
        _searchByTitle.value = search.trim()
    }

    internal fun clearSearchTitle() {
        if (_searchByTitle.value.isNotEmpty()) {
            _searchByTitle.value = ""
            _comicsList.value = emptyList()
            loadComics()
        }
    }

    internal fun searchByTitle() {
        Timber.i("searchSearchByTitle $_searchByTitle")
        _comicsList.value = emptyList()
        loadComics()
    }

    private fun loadComics() {
        _comicsViewModelState.value = ComicsViewModelState.Loading
        viewModelScope.launch {
            comicsRepository.getRemoteComics(
                title = _searchByTitle.value,
                offset = offset,
                onError = { messageId -> onComicsFailed(messageId = messageId) },
                onSuccess = { result -> onComicsLoaded(result) })
        }

    }

    internal fun loadMoreComics() {
        if (_comicsViewModelState.value is ComicsViewModelState.IsLoaded || _comicsViewModelState.value is ComicsViewModelState.OnError) {
            _comicsViewModelState.value = ComicsViewModelState.Loading
            if (totalCount > loadedItems)
                viewModelScope.launch {
                    comicsRepository.getRemoteComics(
                        title = _searchByTitle.value,
                        offset = offset + LOAD_SIZE,
                        onError = { messageId -> onComicsFailed(messageId = messageId) },
                        onSuccess = { result -> onMoreComicsLoaded(result) })
                }
            else
                _comicsViewModelState.value = ComicsViewModelState.AllLoaded
        }
    }

    @VisibleForTesting
    fun onComicsLoaded(comicsResult: ComicsResult) {
        totalCount = comicsResult.comics?.total ?: 0
        loadedItems = comicsResult.comics?.count ?: 0
        offset = comicsResult.comics?.offset ?: 0
        _comicsList.value = comicsResult.comics?.results ?: emptyList()
        _comicsViewModelState.value = ComicsViewModelState.IsLoaded
    }

    @VisibleForTesting
    fun onMoreComicsLoaded(comicsResult: ComicsResult) {
        loadedItems += comicsResult.comics?.count ?: 0
        offset = comicsResult.comics?.offset ?: 0
        _comicsList.value = ArrayList<Comic>().apply {
            addAll(_comicsList.value ?: emptyList())
            addAll(comicsResult.comics?.results ?: emptyList())
        }
        _comicsViewModelState.value = ComicsViewModelState.IsLoaded
    }

    @VisibleForTesting
    fun onComicsFailed(messageId: Int) {
        _comicsViewModelState.value = ComicsViewModelState.OnError(messageId = messageId)
    }
}