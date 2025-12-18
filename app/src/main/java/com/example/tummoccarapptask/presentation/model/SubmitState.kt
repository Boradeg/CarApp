package com.example.tummoccarapptask.presentation.model

sealed class SubmitState {
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}