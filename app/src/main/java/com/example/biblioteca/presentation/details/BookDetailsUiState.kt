package com.example.biblioteca.presentation.details

import com.example.biblioteca.domain.model.BookDetails

data class BookDetailsUiState (
    val bookDetails: BookDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)