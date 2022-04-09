package com.miled.marvel.features.comics.viewModel

sealed class ComicsViewModelState {
    object IsLoaded : ComicsViewModelState()

    object Initial : ComicsViewModelState()

    object Loading : ComicsViewModelState()

    data class OnError(val messageId: Int) : ComicsViewModelState()

    object AllLoaded : ComicsViewModelState()
}