package com.example.biblioteca.data.repository

import com.example.biblioteca.data.local.dao.BookDao
import com.example.biblioteca.data.mapper.toDomain
import com.example.biblioteca.data.mapper.toEntity
import com.example.biblioteca.data.mapper.toLibraryBook
import com.example.biblioteca.data.remote.api.OpenLibraryApi
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: OpenLibraryApi,
    private val bookDao: BookDao
): BookRepository {

    override suspend fun searchBooks(
        query: String,
        page: Int,
        limit: Int
    ): Result<List<Book>> {
        return try {
            val response = api.searchBooks(
                query = query,
                page = page,
                limit = limit
            )
            val books = response.docs
                .mapNotNull { dto ->
                    if (dto.key.isNullOrBlank()) {
                        null
                    } else {
                        dto.toDomain()
                    }
                }
            Result.success(books)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    override suspend fun getBookDetails(bookId: String): Result<BookDetails> {
        return try {
            val normalizedId = bookId
                .removePrefix("/works/")

            val response = api.getBookDetails(
                workId = normalizedId
            )

            Result.success(response.toDomain())

        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    override suspend fun saveBook(book: Book) {
        val existingBook = bookDao.getBookById(book.id)
        val entity = if (existingBook != null){
            book.toEntity(
                status = existingBook.status
            ).copy(
                currentPage = existingBook.currentPage,
                rating = existingBook.rating,
                notes = existingBook.notes,
                startedAt = existingBook.startedAt,
                finishedAt = existingBook.finishedAt,
                savedAt = existingBook.savedAt
            )
        }else {
            book.toEntity()
        }
        bookDao.insertBook(entity)
    }

    override suspend fun removeBook(bookId: String) {
        val book = bookDao.getBookById(bookId)
        if (book != null){
            bookDao.deleteBook(book)
        }
    }

    override suspend fun getSavedBook(bookId: String): LibraryBook? {
        return bookDao
            .getBookById(bookId)
            ?.toLibraryBook()
    }

    override fun observeSavedBooks(): Flow<List<LibraryBook>> {
        return bookDao
            .observeAllBooks()
            .map { books ->
                books.map { it.toLibraryBook() }
            }
    }

    override fun observeBooksByStatus(status: ReadingStatus): Flow<List<LibraryBook>> {
        return bookDao
            .observeBooksByStatus(status)
            .map { books ->
                books.map { it.toLibraryBook() }
            }
    }

    override fun searchSavedBooks(query: String): Flow<List<LibraryBook>> {
        return bookDao
            .searchSavedBooks(query)
            .map { books ->
                books.map { it.toLibraryBook() }
            }
    }

    override suspend fun updateReadingProgress(bookId: String, currentPage: Int) {
        bookDao.updateReadingProgress(
            bookId = bookId,
            currentPage = currentPage
        )
    }

    override suspend fun updateReadingStatus(
        bookId: String,
        status: ReadingStatus
    ) {
        bookDao.updateReadingStatus(
            bookId = bookId,
            status = status
        )
    }

    override suspend fun updateRating(bookId: String, rating: Float?) {
        bookDao.updateRating(
            bookId = bookId,
            rating = rating
        )
    }

    override suspend fun updateNotes(bookId: String, notes: String?) {
        bookDao.updateNotes(
            bookId = bookId,
            notes = notes
        )
    }
}