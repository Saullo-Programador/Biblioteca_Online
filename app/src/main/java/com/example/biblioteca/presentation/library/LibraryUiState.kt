package com.example.biblioteca.presentation.library

import com.example.biblioteca.domain.model.LibraryBook

data class LibraryUiState (
    val books: List<LibraryBook> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val message: String? = null
) {
    val filteredBooks: List<LibraryBook>
        get() {
            val normalizedQuery = query.trim()

            if (normalizedQuery.isBlank()) return books

            return books.filter { libraryBook ->
                libraryBook.book.title.contains(
                    normalizedQuery,
                    ignoreCase = true
                ) || libraryBook.book.authors.any { author ->
                    author.contains(normalizedQuery, ignoreCase = true)
                }
            }
        }
}