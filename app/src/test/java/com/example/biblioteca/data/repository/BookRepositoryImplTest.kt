package com.example.biblioteca.data.repository

import com.example.biblioteca.data.local.dao.BookDao
import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.data.remote.api.OpenLibraryApi
import com.example.biblioteca.data.remote.dto.BookDetailsDto
import com.example.biblioteca.data.remote.dto.BookDto
import com.example.biblioteca.data.remote.dto.BookSearchResponseDto
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookRepositoryImplTest {

    private lateinit var api: FakeOpenLibraryApi
    private lateinit var dao: FakeBookDao
    private lateinit var repository: BookRepositoryImpl

    @Before
    fun setUp() {
        api = FakeOpenLibraryApi()
        dao = FakeBookDao()
        repository = BookRepositoryImpl(api, dao)
    }

    @Test
    fun `searchBooks returns mapped books`() = runTest {
        api.searchResponse = BookSearchResponseDto(
            docs = listOf(
                BookDto(
                    key = "/works/OL123W",
                    title = "Clean Code",
                    authors = listOf("Robert C. Martin"),
                    firstPublishYear = 2008,
                    isbns = listOf("9780132350884"),
                    coverId = 123,
                    publishers = listOf("Prentice Hall"),
                    numberOfPages = 464,
                    subjects = listOf("Programming")
                )
            )
        )

        val result = repository.searchBooks(
            query = "Clean Code",
            page = 2,
            limit = 10
        )

        assertTrue(result.isSuccess)

        val books = result.getOrThrow()

        assertEquals(1, books.size)
        assertEquals("OL123W", books.first().id)
        assertEquals("Clean Code", books.first().title)
        assertEquals(listOf("Robert C. Martin"), books.first().authors)
        assertEquals(123, books.first().coverId)
        assertEquals(2008, books.first().firstPublishYear)
        assertEquals("9780132350884", books.first().isbn)
        assertEquals("Prentice Hall", books.first().publisher)
        assertEquals(464, books.first().numberOfPages)
        assertEquals(listOf("Programming"), books.first().subjects)

        assertEquals("Clean Code", api.lastSearchQuery)
        assertEquals(2, api.lastSearchPage)
        assertEquals(10, api.lastSearchLimit)
    }

    @Test
    fun `searchBooks ignores books without a key`() = runTest {
        api.searchResponse = BookSearchResponseDto(
            docs = listOf(
                BookDto(
                    key = "/works/OL123W",
                    title = "Valid book"
                ),
                BookDto(
                    key = null,
                    title = "Book without key"
                ),
                BookDto(
                    key = "",
                    title = "Book with empty key"
                )
            )
        )

        val result = repository.searchBooks("books")

        assertTrue(result.isSuccess)

        val books = result.getOrThrow()

        assertEquals(1, books.size)
        assertEquals("OL123W", books.first().id)
    }

    @Test
    fun `searchBooks returns failure when API throws`() = runTest {
        api.searchException = IllegalStateException("API unavailable")

        val result = repository.searchBooks("Kotlin")

        assertTrue(result.isFailure)
        assertEquals(
            "API unavailable",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `getBookDetails removes works prefix before API call`() = runTest {
        api.detailsResponse = BookDetailsDto(
            key = "/works/OL456W",
            title = "Domain-Driven Design",
            description = "A book about domain-driven design.",
            firstPublishDate = "2003",
            covers = listOf(456),
            subjects = listOf("Software Engineering"),
            authors = emptyList()
        )

        val result = repository.getBookDetails("/works/OL456W")

        assertTrue(result.isSuccess)

        val details = result.getOrThrow()

        assertEquals("OL456W", api.lastDetailsWorkId)
        assertEquals("OL456W", details.id)
        assertEquals("Domain-Driven Design", details.title)
        assertEquals(
            "A book about domain-driven design.",
            details.description
        )
        assertEquals("2003", details.firstPublishDate)
        assertEquals(456, details.coverId)
        assertEquals(
            listOf("Software Engineering"),
            details.subjects
        )
    }

    @Test
    fun `getBookDetails returns failure when API throws`() = runTest {
        api.detailsException = IllegalStateException("Book not found")

        val result = repository.getBookDetails("OL456W")

        assertTrue(result.isFailure)
        assertEquals(
            "Book not found",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `saveBook inserts new book with default status`() = runTest {
        val book = createBook()

        repository.saveBook(book)

        val saved = dao.books[book.id]

        assertEquals(book.id, saved?.id)
        assertEquals(book.title, saved?.title)
        assertEquals(
            ReadingStatus.WANT_TO_READ,
            saved?.status
        )
        assertEquals(0, saved?.currentPage)
        assertNull(saved?.rating)
        assertNull(saved?.notes)
    }

    @Test
    fun `saveBook preserves reading data when book already exists`() = runTest {
        val existing = createEntity(
            status = ReadingStatus.READING,
            currentPage = 120,
            rating = 4.5f,
            notes = "Continue from chapter 5",
            startedAt = 1000L,
            finishedAt = null,
            savedAt = 2000L
        )

        dao.books[existing.id] = existing

        val updatedBook = createBook(
            title = "Updated title"
        )

        repository.saveBook(updatedBook)

        val saved = dao.books[existing.id]

        assertEquals("Updated title", saved?.title)
        assertEquals(ReadingStatus.READING, saved?.status)
        assertEquals(120, saved?.currentPage)
        assertEquals(4.5f, saved?.rating)
        assertEquals("Continue from chapter 5", saved?.notes)
        assertEquals(1000L, saved?.startedAt)
        assertNull(saved?.finishedAt)
        assertEquals(2000L, saved?.savedAt)
    }

    @Test
    fun `removeBook deletes existing book`() = runTest {
        val entity = createEntity()
        dao.books[entity.id] = entity

        repository.removeBook(entity.id)

        assertFalse(dao.books.containsKey(entity.id))
    }

    @Test
    fun `removeBook does nothing when book does not exist`() = runTest {
        repository.removeBook("missing-book")

        assertTrue(dao.books.isEmpty())
    }

    @Test
    fun `getSavedBook returns null when book does not exist`() = runTest {
        val result = repository.getSavedBook("missing-book")

        assertNull(result)
    }

    @Test
    fun `updateReadingProgress delegates to DAO`() = runTest {
        repository.updateReadingProgress("OL123W", 75)

        assertEquals("OL123W", dao.lastUpdatedBookId)
        assertEquals(75, dao.lastUpdatedPage)
    }

    @Test
    fun `updateReadingStatus delegates to DAO`() = runTest {
        repository.updateReadingStatus(
            bookId = "OL123W",
            status = ReadingStatus.COMPLETED
        )

        assertEquals("OL123W", dao.lastUpdatedBookId)
        assertEquals(ReadingStatus.COMPLETED, dao.lastUpdatedStatus)
    }

    @Test
    fun `updateRating delegates to DAO`() = runTest {
        repository.updateRating("OL123W", 4.5f)

        assertEquals("OL123W", dao.lastUpdatedBookId)
        assertEquals(4.5f, dao.lastUpdatedRating)
    }

    @Test
    fun `updateRating accepts null`() = runTest {
        repository.updateRating("OL123W", null)

        assertEquals("OL123W", dao.lastUpdatedBookId)
        assertNull(dao.lastUpdatedRating)
    }

    @Test
    fun `updateNotes delegates to DAO`() = runTest {
        repository.updateNotes(
            bookId = "OL123W",
            notes = "Excellent book"
        )

        assertEquals("OL123W", dao.lastUpdatedBookId)
        assertEquals("Excellent book", dao.lastUpdatedNotes)
    }

    private fun createBook(
        id: String = "OL123W",
        title: String = "Clean Code"
    ) = Book(
        id = id,
        title = title,
        authors = listOf("Robert C. Martin"),
        coverId = 123,
        firstPublishYear = 2008,
        isbn = "9780132350884",
        publisher = "Prentice Hall",
        numberOfPages = 464,
        subjects = listOf("Programming")
    )

    private fun createEntity(
        id: String = "OL123W",
        status: ReadingStatus = ReadingStatus.WANT_TO_READ,
        currentPage: Int = 0,
        rating: Float? = null,
        notes: String? = null,
        startedAt: Long? = null,
        finishedAt: Long? = null,
        savedAt: Long = 1000L
    ) = BookEntity(
        id = id,
        title = "Clean Code",
        authors = listOf("Robert C. Martin"),
        coverId = 123,
        firstPublishYear = 2008,
        isbn = "9780132350884",
        publisher = "Prentice Hall",
        numberOfPages = 464,
        subjects = listOf("Programming"),
        status = status,
        currentPage = currentPage,
        rating = rating,
        notes = notes,
        startedAt = startedAt,
        finishedAt = finishedAt,
        savedAt = savedAt
    )

    private class FakeOpenLibraryApi : OpenLibraryApi {

        var searchResponse = BookSearchResponseDto(
            docs = emptyList()
        )

        var detailsResponse = BookDetailsDto()

        var searchException: Exception? = null
        var detailsException: Exception? = null

        var lastSearchQuery: String? = null
        var lastSearchPage: Int? = null
        var lastSearchLimit: Int? = null
        var lastDetailsWorkId: String? = null

        override suspend fun searchBooks(
            query: String,
            page: Int,
            limit: Int
        ): BookSearchResponseDto {
            lastSearchQuery = query
            lastSearchPage = page
            lastSearchLimit = limit

            searchException?.let { throw it }

            return searchResponse
        }

        override suspend fun searchBooksByTitle(
            title: String,
            page: Int,
            limit: Int
        ) = BookSearchResponseDto(docs = emptyList())

        override suspend fun searchBooksByAuthor(
            author: String,
            page: Int,
            limit: Int
        ) = BookSearchResponseDto(docs = emptyList())

        override suspend fun searchBooksByIsbn(
            isbn: String
        ) = BookSearchResponseDto(docs = emptyList())

        override suspend fun getBookDetails(
            workId: String
        ): BookDetailsDto {
            lastDetailsWorkId = workId

            detailsException?.let { throw it }

            return detailsResponse
        }

        override suspend fun getAuthor(authorId: String) =
            throw UnsupportedOperationException("Not used in this test")

        override suspend fun getAuthorWorks(
            authorId: String,
            limit: Int
        ) = BookSearchResponseDto(docs = emptyList())
    }

    private class FakeBookDao : BookDao {

        val books = mutableMapOf<String, BookEntity>()

        var lastUpdatedBookId: String? = null
        var lastUpdatedPage: Int? = null
        var lastUpdatedStatus: ReadingStatus? = null
        var lastUpdatedRating: Float? = null
        var lastUpdatedNotes: String? = null

        override suspend fun insertBook(book: BookEntity) {
            books[book.id] = book
        }

        override suspend fun insertBooks(book: List<BookEntity>) {
            book.forEach { books[it.id] = it }
        }

        override suspend fun updateBook(book: BookEntity) {
            books[book.id] = book
        }

        override suspend fun deleteBook(book: BookEntity) {
            books.remove(book.id)
        }

        override fun observeBookById(
            bookId: String
        ): Flow<BookEntity?> = flowOf(books[bookId])

        override suspend fun getBookById(
            bookId: String
        ): BookEntity? = books[bookId]

        override fun observeAllBooks(): Flow<List<BookEntity>> =
            flowOf(books.values.toList())

        override fun observeBooksByStatus(
            status: ReadingStatus
        ): Flow<List<BookEntity>> =
            flowOf(books.values.filter { it.status == status })

        override fun searchSavedBooks(
            query: String
        ): Flow<List<BookEntity>> =
            flowOf(
                books.values.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.authors.any { author ->
                                author.contains(query, ignoreCase = true)
                            }
                }
            )

        override fun observeBooksByRating(): Flow<List<BookEntity>> =
            flowOf(books.values.sortedByDescending { it.rating })

        override suspend fun updateReadingProgress(
            bookId: String,
            currentPage: Int
        ) {
            lastUpdatedBookId = bookId
            lastUpdatedPage = currentPage
            books[bookId]?.let {
                books[bookId] = it.copy(currentPage = currentPage)
            }
        }

        override suspend fun updateReadingStatus(
            bookId: String,
            status: ReadingStatus
        ) {
            lastUpdatedBookId = bookId
            lastUpdatedStatus = status
            books[bookId]?.let {
                books[bookId] = it.copy(status = status)
            }
        }

        override suspend fun updateRating(
            bookId: String,
            rating: Float?
        ) {
            lastUpdatedBookId = bookId
            lastUpdatedRating = rating
            books[bookId]?.let {
                books[bookId] = it.copy(rating = rating)
            }
        }

        override suspend fun updateNotes(
            bookId: String,
            notes: String?
        ) {
            lastUpdatedBookId = bookId
            lastUpdatedNotes = notes
            books[bookId]?.let {
                books[bookId] = it.copy(notes = notes)
            }
        }

        override suspend fun deleteAllBooks() {
            books.clear()
        }
    }
}
