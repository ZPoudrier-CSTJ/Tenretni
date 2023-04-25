package com.example.tenretni.ui.loading

sealed class LoadingUiState {
    class Loading(val progression: Int): LoadingUiState()
    object Finished: LoadingUiState()
}