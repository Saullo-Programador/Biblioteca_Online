package com.example.biblioteca.presentation.home

import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus

data class HomeUiState (
    val books: List<LibraryBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
){
    val totalBooks: Int
        get() = books.size

    val bookReading: Int
        get() = books.count { it.status == ReadingStatus.READING }

    val bookCompleted: Int
        get() = books.count { it.status == ReadingStatus.COMPLETED }

    val booksToRead: Int
        get() = books.count { it.status == ReadingStatus.WANT_TO_READ }

    val recentlySavedBook: List<LibraryBook>
        get() = books.sortedByDescending { it.savedAt }.take(5)
}