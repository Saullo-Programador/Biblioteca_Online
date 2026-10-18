package com.example.biblioteca.presentation.details

import android.os.Message

data class SaveBookUiState (
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val message: String? = null
)