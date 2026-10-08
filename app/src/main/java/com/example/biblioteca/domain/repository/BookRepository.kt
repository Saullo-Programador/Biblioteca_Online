package com.example.biblioteca.domain.repository

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBooks(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Book>>

    suspend fun getBookDetails(
        bookId: String
    ): Result<BookDetails>

    suspend fun saveBook(
        book: Book
    )

    suspend fun removeBook(
        bookId: String
    )

    suspend fun getSavedBook(
        bookId: String
    ): Book?

    fun observeSavedBook(): Flow<List<Book>>

    fun observeBooksByStatus(
        status: ReadingStatus
    ): Flow<List<Book>>

    fun searchSaveBooks(
        query: String
    ): Flow<List<Book>>

    suspend fun updateReadingProgress(
        bookId: String,
        currentPage: Int
    )

    suspend fun updateReadingStatus(
        bookId: String,
        status: ReadingStatus
    )

    suspend fun updateRating(
        bookId: String,
        rating: Float?
    )

    suspend fun updateNotes(
        bookId: String,
        notes: String?
    )
}