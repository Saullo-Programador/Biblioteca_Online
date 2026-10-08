package com.example.biblioteca.fake

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBookRepository : BookRepository {

    private val books = MutableStateFlow<List<Book>>(emptyList())

    var searchResult: Result<List<Book>> =
        Result.success(emptyList())

    var bookDetailsResult: Result<BookDetails> =
        Result.failure(
            IllegalStateException("Nenhum detalhe configurado")
        )

    var lastSavedBook: Book? = null
    var lastRemovedBookId: String? = null

    var lastUpdatedBookId: String? = null
    var lastUpdatedPage: Int? = null

    var lastUpdatedStatus: ReadingStatus? = null

    var lastUpdatedRating: Float? = null

    var lastUpdatedNotes: String? = null

    override suspend fun searchBooks(
        query: String,
        page: Int,
        limit: Int
    ): Result<List<Book>> {
        return searchResult
    }

    override suspend fun getBookDetails(
        bookId: String
    ): Result<BookDetails> {
        return bookDetailsResult
    }

    override suspend fun saveBook(book: Book) {
        lastSavedBook = book

        books.value = books.value
            .filterNot { it.id == book.id } + book
    }

    override suspend fun removeBook(bookId: String) {
        lastRemovedBookId = bookId

        books.value = books.value
            .filterNot { it.id == bookId }
    }

    override suspend fun getSavedBook(
        bookId: String
    ): Book? {
        return books.value.firstOrNull {
            it.id == bookId
        }
    }

    override fun observeSavedBooks(): Flow<List<Book>> {
        return books
    }

    override fun observeBooksByStatus(
        status: ReadingStatus
    ): Flow<List<Book>> {
        return books.map { bookList ->
            bookList.filter {
                // O domínio Book atualmente não possui status.
                // Por isso, este Fake será ajustado quando
                // o modelo de domínio receber essa informação.
                true
            }
        }
    }

    override fun searchSaveBooks(query: String): Flow<List<Book>> {
        return books.map { bookList ->
            bookList.filter { book ->
                book.title.contains(
                    query,
                    ignoreCase = true
                ) ||
                        book.authors.any {
                            it.contains(
                                query,
                                ignoreCase = true
                            )
                        }
            }
        }
    }

    override suspend fun updateReadingProgress(
        bookId: String,
        currentPage: Int
    ) {
        lastUpdatedBookId = bookId
        lastUpdatedPage = currentPage
    }

    override suspend fun updateReadingStatus(
        bookId: String,
        status: ReadingStatus
    ) {
        lastUpdatedBookId = bookId
        lastUpdatedStatus = status
    }

    override suspend fun updateRating(
        bookId: String,
        rating: Float?
    ) {
        lastUpdatedBookId = bookId
        lastUpdatedRating = rating
    }

    override suspend fun updateNotes(
        bookId: String,
        notes: String?
    ) {
        lastUpdatedBookId = bookId
        lastUpdatedNotes = notes
    }
}