package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.remote.dto.AuthorKeyDto
import com.example.biblioteca.data.remote.dto.AuthorReferenceDto
import com.example.biblioteca.data.remote.dto.BookDetailsDto
import com.example.biblioteca.data.remote.dto.BookDto
import com.example.biblioteca.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookDtoMapperTest {

    @Test
    fun `deve converter BookDto para Book preservando os dados`() {
        val dto = BookDto(
            key = "/works/OL123W",
            title = "Clean Code",
            authors = listOf("Robert C. Martin"),
            authorKeys = listOf("/authors/OL456A"),
            firstPublishYear = 2008,
            isbns = listOf("9780132350884"),
            coverId = 123,
            editionKeys = listOf("OL789M"),
            publishers = listOf("Prentice Hall"),
            languages = listOf("eng"),
            numberOfPages = 464,
            subjects = listOf("Programming", "Software Engineering")
        )

        val result = dto.toDomain()

        assertEquals("OL123W", result.id)
        assertEquals("Clean Code", result.title)
        assertEquals(listOf("Robert C. Martin"), result.authors)
        assertEquals(123, result.coverId)
        assertEquals(2008, result.firstPublishYear)
        assertEquals("9780132350884", result.isbn)
        assertEquals("Prentice Hall", result.publisher)
        assertEquals(464, result.numberOfPages)
        assertEquals(
            listOf("Programming", "Software Engineering"),
            result.subjects
        )
    }

    @Test
    fun `deve converter BookDto para Book com campos opcionais nulos`() {
        val dto = BookDto(
            key = "/works/OL456W",
            title = "Livro sem metadados"
        )

        val result = dto.toDomain()

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
    fun `deve usar valores padrao quando campos do BookDto forem nulos`() {
        val dto = BookDto(
            key = null,
            title = null,
            authors = null,
            firstPublishYear = null,
            isbns = null,
            publishers = null,
            subjects = null
        )

        val result = dto.toDomain()

        assertEquals("", result.id)
        assertEquals("", result.title)
        assertEquals(emptyList<String>(), result.authors)
        assertNull(result.isbn)
        assertNull(result.publisher)
        assertEquals(emptyList<String>(), result.subjects)
    }

    @Test
    fun `deve selecionar o primeiro ISBN e a primeira editora`() {
        val dto = BookDto(
            key = "/works/OL789W",
            title = "Domain-Driven Design",
            isbns = listOf("ISBN-PRIMEIRO", "ISBN-SEGUNDO"),
            publishers = listOf("Editora A", "Editora B")
        )

        val result = dto.toDomain()

        assertEquals("ISBN-PRIMEIRO", result.isbn)
        assertEquals("Editora A", result.publisher)
    }

    @Test
    fun `deve converter BookDto para BookEntity`() {
        val dto = BookDto(
            key = "/works/OL111W",
            title = "Clean Architecture",
            authors = listOf("Robert C. Martin"),
            firstPublishYear = 2017,
            coverId = 321,
            publishers = listOf("Prentice Hall")
        )

        val result = dto.toEntity(ReadingStatus.READING)

        assertEquals("OL111W", result.id)
        assertEquals("Clean Architecture", result.title)
        assertEquals(listOf("Robert C. Martin"), result.authors)
        assertEquals(2017, result.firstPublishYear)
        assertEquals(321, result.coverId)
        assertEquals("Prentice Hall", result.publisher)
        assertEquals(ReadingStatus.READING, result.status)
    }

    @Test
    fun `deve usar status WANT_TO_READ por padrao ao criar entidade`() {
        val dto = BookDto(
            key = "/works/OL222W",
            title = "Livro novo"
        )

        val result = dto.toEntity()

        assertEquals(ReadingStatus.WANT_TO_READ, result.status)
    }

    @Test
    fun `deve converter BookDetailsDto para BookDetails`() {
        val dto = BookDetailsDto(
            key = "/works/OL333W",
            title = "Clean Architecture",
            description = "Um livro sobre arquitetura de software",
            firstPublishDate = "2017",
            covers = listOf(456, 789),
            subjects = listOf("Architecture", "Software Engineering"),
            authors = listOf(
                AuthorReferenceDto(
                    author = AuthorKeyDto(
                        key = "/authors/OL987A"
                    )
                )
            )
        )

        val result = dto.toDomain()

        assertEquals("OL333W", result.id)
        assertEquals("Clean Architecture", result.title)
        assertEquals(
            "Um livro sobre arquitetura de software",
            result.description
        )
        assertEquals("2017", result.firstPublishDate)
        assertEquals(456, result.coverId)
        assertEquals(
            listOf("Architecture", "Software Engineering"),
            result.subjects
        )
        assertEquals(listOf("OL987A"), result.authorIds)
    }

    @Test
    fun `deve preservar campos nulos de BookDetailsDto`() {
        val dto = BookDetailsDto(
            key = "/works/OL444W",
            title = "Livro sem descrição"
        )

        val result = dto.toDomain()

        assertEquals("OL444W", result.id)
        assertEquals("Livro sem descrição", result.title)
        assertNull(result.description)
        assertNull(result.firstPublishDate)
        assertNull(result.coverId)
        assertEquals(emptyList<String>(), result.subjects)
        assertEquals(emptyList<String>(), result.authorIds)
    }

    @Test
    fun `deve ignorar referencias de autores sem chave`() {
        val dto = BookDetailsDto(
            key = "/works/OL555W",
            title = "Livro com referências incompletas",
            authors = listOf(
                AuthorReferenceDto(
                    author = AuthorKeyDto(key = "/authors/OL123A")
                ),
                AuthorReferenceDto(author = null),
                AuthorReferenceDto(
                    author = AuthorKeyDto(key = "/authors/OL456A")
                )
            )
        )

        val result = dto.toDomain()

        assertEquals(
            listOf("OL123A", "OL456A"),
            result.authorIds
        )
    }

    @Test
    fun `deve usar valores padrao quando campos principais de BookDetailsDto forem nulos`() {
        val dto = BookDetailsDto()

        val result = dto.toDomain()

        assertEquals("", result.id)
        assertEquals("", result.title)
        assertNull(result.description)
        assertNull(result.firstPublishDate)
        assertNull(result.coverId)
        assertEquals(emptyList<String>(), result.subjects)
        assertEquals(emptyList<String>(), result.authorIds)
    }
}