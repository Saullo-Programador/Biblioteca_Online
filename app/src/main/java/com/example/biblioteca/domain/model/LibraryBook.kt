package com.example.biblioteca.domain.model

data class LibraryBook(
    val book: Book,
    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val currentPage: Int = 0,
    val rating: Float? = null,
    val notes: String? = null,
    val startedAt: Long? = null,
    val finishedAt: Long? = null,
    val savedAt: Long = System.currentTimeMillis()
)