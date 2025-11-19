package com.example.intervalsapp.presentation

sealed interface UiLoadState {
    object Idle: UiLoadState
    object Loading: UiLoadState
    object Loaded: UiLoadState
    data class Error(val message: String): UiLoadState
}