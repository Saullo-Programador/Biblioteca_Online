package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.entity.AuthorEntity
import com.example.biblioteca.data.remote.dto.AuthorDto
import com.example.biblioteca.domain.model.Author
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthorMapperTest {

    @Test
    fun `toDomain maps AuthorDto correctly`() {
        // Given
        val dto = AuthorDto(
            key = "/authors/OL23919A",
            name = "Robert C. Martin",
            birthDate = "1952",
            deathDate = null,
            bio = "Software engineer and author",
            photos = listOf(123, 456)
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(
            Author(
                id = "OL23919A",
                name = "Robert C. Martin",
                birthDate = "1952",
                deathDate = null,
                bio = "Software engineer and author",
                photoId = 123
            ),
            result
        )
    }

    @Test
    fun `toDomain uses defaults when AuthorDto fields are null`() {
        // Given
        val dto = AuthorDto()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(
            Author(
                id = "",
                name = "",
                birthDate = null,
                deathDate = null,
                bio = null,
                photoId = null
            ),
            result
        )
    }

    @Test
    fun `toEntity maps AuthorDto correctly`() {
        // Given
        val dto = AuthorDto(
            key = "/authors/OL123A",
            name = "J. R. R. Tolkien",
            birthDate = "1892",
            deathDate = "1973",
            bio = "English writer",
            photos = listOf(789, 999)
        )

        // When
        val result = dto.toEntity()

        // Then
        assertEquals(
            AuthorEntity(
                id = "OL123A",
                name = "J. R. R. Tolkien",
                birthDate = "1892",
                deathDate = "1973",
                bio = "English writer",
                photoId = 789
            ),
            result
        )
    }

    @Test
    fun `toDomain maps AuthorEntity correctly`() {
        // Given
        val entity = AuthorEntity(
            id = "OL456A",
            name = "Isaac Asimov",
            birthDate = "1920",
            deathDate = "1992",
            bio = "Science fiction writer",
            photoId = 321
        )

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(
            Author(
                id = "OL456A",
                name = "Isaac Asimov",
                birthDate = "1920",
                deathDate = "1992",
                bio = "Science fiction writer",
                photoId = 321
            ),
            result
        )
    }

    @Test
    fun `toDomain preserves empty bio`() {
        // Given
        val dto = AuthorDto(
            key = "/authors/OL123A",
            name = "Author",
            bio = "",
            photos = emptyList()
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals("", result.bio)
        assertEquals(null, result.photoId)
    }
}
