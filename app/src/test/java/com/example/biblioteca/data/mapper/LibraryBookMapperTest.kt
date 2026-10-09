package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test

class LibraryBookMapperTest {

    private fun createBookEntity(): BookEntity {
        return BookEntity(
            id = "OL123W",
            title = "Clean Code",
            authors = listOf("Robert C. Martin"),
            coverId = 123,
            firstPublishYear = 2008,
            isbn = "9780132350884",
            publisher = "Prentice Hall",
            numberOfPages = 464,
            subjects = listOf("Programming", "Software Engineering"),
            status = ReadingStatus.READING,
            currentPage = 150,
            rating = 4.5f,
            notes = "Livro muito interessante",
            startedAt = 1_760_000_000_000L,
            finishedAt = null,
            savedAt = 1_759_000_000_000L
        )
    }

    @Test
    fun `deve converter BookEntity para LibraryBook preservando os dados`() {
        // Given
        val entity = createBookEntity()

        // When
        val result = entity.toLibraryBook()

        // Then
        assertEquals(entity.id, result.book.id)
        assertEquals(entity.title, result.book.title)
        assertEquals(entity.authors, result.book.authors)
        assertEquals(entity.coverId, result.book.coverId)
        assertEquals(entity.firstPublishYear, result.book.firstPublishYear)
        assertEquals(entity.status, result.status)
        assertEquals(entity.currentPage, result.currentPage)
        assertEquals(entity.rating, result.rating)
        assertEquals(entity.notes, result.notes)
        assertEquals(entity.startedAt, result.startedAt)
        assertEquals(entity.finishedAt, result.finishedAt)
        assertEquals(entity.savedAt, result.savedAt)
    }

    @Test
    fun `deve converter LibraryBook para BookEntity preservando os dados`() {
        // Given
        val libraryBook = createBookEntity().toLibraryBook()

        // When
        val result = libraryBook.toEntity()

        // Then
        assertEquals(libraryBook.book.id, result.id)
        assertEquals(libraryBook.book.title, result.title)
        assertEquals(libraryBook.book.authors, result.authors)
        assertEquals(libraryBook.book.coverId, result.coverId)
        assertEquals(libraryBook.book.firstPublishYear, result.firstPublishYear)
        assertEquals(libraryBook.book.isbn, result.isbn)
        assertEquals(libraryBook.book.publisher, result.publisher)
        assertEquals(libraryBook.book.numberOfPages, result.numberOfPages)
        assertEquals(libraryBook.book.subjects, result.subjects)
        assertEquals(libraryBook.status, result.status)
        assertEquals(libraryBook.currentPage, result.currentPage)
        assertEquals(libraryBook.rating, result.rating)
        assertEquals(libraryBook.notes, result.notes)
        assertEquals(libraryBook.startedAt, result.startedAt)
        assertEquals(libraryBook.finishedAt, result.finishedAt)
        assertEquals(libraryBook.savedAt, result.savedAt)
    }

    @Test
    fun `deve preservar campos opcionais nulos`() {
        // Given
        val entity = createBookEntity().copy(
            rating = null,
            notes = null,
            startedAt = null,
            finishedAt = null
        )

        // When
        val result = entity.toLibraryBook()

        // Then
        assertNull(result.rating)
        assertNull(result.notes)
        assertNull(result.startedAt)
        assertNull(result.finishedAt)
    }

    @Test
    fun `deve preservar status de livro concluido`() {
        // Given
        val entity = createBookEntity().copy(
            status = ReadingStatus.COMPLETED,
            currentPage = 464,
            finishedAt = 1_770_000_000_000L
        )

        // When
        val result = entity.toLibraryBook()

        // Then
        assertEquals(ReadingStatus.COMPLETED, result.status)
        assertEquals(464, result.currentPage)
        assertNotNull(result.finishedAt)
    }
}
