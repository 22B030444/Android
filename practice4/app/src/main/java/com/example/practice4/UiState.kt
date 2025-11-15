package com.example.practice4

sealed class UiState {
    object Loading : UiState()
    data class Success(val tracks: List<Track>) : UiState()
    data class Error(val message: String) : UiState()
}