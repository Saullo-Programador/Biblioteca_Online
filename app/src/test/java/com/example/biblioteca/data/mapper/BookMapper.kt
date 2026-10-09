package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookMapperTest {

    @Test
    fun `deve converter BookEntity para Book preservando os dados`() {
        val entity = BookEntity(
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
            notes = "Livro interessante",
            startedAt = 1_760_000_000_000L,
            finishedAt = null,
            savedAt = 1_759_000_000_000L
        )

        val result = entity.toDomain()

        assertEquals(entity.id, result.id)
        assertEquals(entity.title, result.title)
        assertEquals(entity.authors, result.authors)
        assertEquals(entity.coverId, result.coverId)
        assertEquals(entity.firstPublishYear, result.firstPublishYear)
        assertEquals(entity.isbn, result.isbn)
        assertEquals(entity.publisher, result.publisher)
        assertEquals(entity.numberOfPages, result.numberOfPages)
        assertEquals(entity.subjects, result.subjects)
    }

    @Test
    fun `deve preservar campos opcionais nulos ao converter BookEntity`() {
        val entity = BookEntity(
            id = "OL456W",
            title = "Livro sem metadados",
            authors = emptyList(),
            coverId = null,
            firstPublishYear = null,
            isbn = null,
            publisher = null,
            numberOfPages = null,
            subjects = emptyList()
        )

        val result = entity.toDomain()

        assertEquals("OL456W", result.id)
        assertEquals("Livro sem metadados", result.title)
        assertEquals(emptyList<String>(), result.authors)
        assertNull(result.coverId)
        assertNull(result.firstPublishYear)
        assertNull(result.isbn)
        assertNull(result.publisher)
        assertNull(result.numberOfPages)
        assertEquals(emptyList<String>(), result.subjects)
    }

    @Test
    fun `deve manter os dados do modelo Book`() {
        val book = Book(
            id = "OL789W",
            title = "Domain-Driven Design",
            authors = listOf("Eric Evans"),
            coverId = 789,
            firstPublishYear = 2003,
            isbn = "9780321125217",
            publisher = "Addison-Wesley",
            numberOfPages = 560,
            subjects = listOf("Software Engineering")
        )

        assertEquals("OL789W", book.id)
        assertEquals("Domain-Driven Design", book.title)
        assertEquals(listOf("Eric Evans"), book.authors)
        assertEquals(789, book.coverId)
        assertEquals(2003, book.firstPublishYear)
        assertEquals("9780321125217", book.isbn)
        assertEquals("Addison-Wesley", book.publisher)
        assertEquals(560, book.numberOfPages)
        assertEquals(listOf("Software Engineering"), book.subjects)
    }

    @Test
    fun `deve preservar campos opcionais nulos em BookDetails`() {
        val details = BookDetails(
            id = "OL999W",
            title = "Livro sem descrição",
            description = null,
            firstPublishDate = null,
            coverId = null,
            subjects = emptyList(),
            authorIds = emptyList()
        )

        assertEquals("OL999W", details.id)
        assertEquals("Livro sem descrição", details.title)
        assertNull(details.description)
        assertNull(details.firstPublishDate)
        assertNull(details.coverId)
        assertEquals(emptyList<String>(), details.subjects)
        assertEquals(emptyList<String>(), details.authorIds)
    }
}
