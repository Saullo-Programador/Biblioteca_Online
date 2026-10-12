package com.example.biblioteca.presentation.search

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.model.Book

data class SearchUiState (
    val query: String = "",
    val books: List<Book> = emptyList(),
    val authors: List<Author> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)