package com.example.biblioteca.presentation.reading

import com.example.biblioteca.domain.model.LibraryBook

data class ReadingUiState(
    val books: List<LibraryBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)