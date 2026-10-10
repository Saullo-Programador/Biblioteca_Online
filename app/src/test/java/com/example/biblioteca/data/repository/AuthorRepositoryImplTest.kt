package com.example.biblioteca.data.repository

import com.example.biblioteca.data.local.dao.AuthorDao
import com.example.biblioteca.data.local.entity.AuthorEntity
import com.example.biblioteca.data.remote.api.OpenLibraryApi
import com.example.biblioteca.data.remote.dto.AuthorDto
import com.example.biblioteca.data.remote.dto.BookDetailsDto
import com.example.biblioteca.data.remote.dto.BookSearchResponseDto
import com.example.biblioteca.domain.model.Author
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthorRepositoryImplTest {

    private lateinit var api: FakeOpenLibraryApi
    private lateinit var dao: FakeAuthorDao
    private lateinit var repository: AuthorRepositoryImpl

    @Before
    fun setUp() {
        api = FakeOpenLibraryApi()
        dao = FakeAuthorDao()
        repository = AuthorRepositoryImpl(api, dao)
    }

    @Test
    fun `getAuthor returns mapped author`() = runTest {
        api.authorResponse = AuthorDto(
            key = "/authors/OL123A",
            name = "J. R. R. Tolkien",
            birthDate = "1892",
            deathDate = "1973",
            bio = "English writer",
            photos = listOf(123, 456)
        )

        val result = repository.getAuthor("/authors/OL123A")

        assertTrue(result.isSuccess)
        assertEquals("OL123A", result.getOrThrow().id)
        assertEquals("J. R. R. Tolkien", result.getOrThrow().name)
        assertEquals("1892", result.getOrThrow().birthDate)
        assertEquals("1973", result.getOrThrow().deathDate)
        assertEquals("English writer", result.getOrThrow().bio)
        assertEquals(123, result.getOrThrow().photoId)
        assertEquals("OL123A", api.lastAuthorId)
    }

    @Test
    fun `getAuthor returns failure when API throws`() = runTest {
        api.authorException = IllegalStateException("API unavailable")

        val result = repository.getAuthor("OL123A")

        assertTrue(result.isFailure)
        assertEquals(
            "API unavailable",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `saveAuthor inserts author into DAO`() = runTest {
        val author = createAuthor()

        repository.saveAuthor(author)

        assertEquals(
            author,
            dao.authors[author.id]?.toDomainForTest()
        )
    }

    @Test
    fun `getSavedAuthor returns author when it exists`() = runTest {
        val author = createAuthor()
        dao.authors[author.id] = author.toEntityForTest()

        val result = repository.getSavedAuthor(author.id)

        assertEquals(author, result)
    }

    @Test
    fun `getSavedAuthor returns null when author does not exist`() = runTest {
        val result = repository.getSavedAuthor("missing-author")

        assertNull(result)
    }

    @Test
    fun `removeAuthor deletes existing author`() = runTest {
        val author = createAuthor()
        dao.authors[author.id] = author.toEntityForTest()

        repository.removeAuthor(author.id)

        assertFalse(dao.authors.containsKey(author.id))
    }

    @Test
    fun `removeAuthor does nothing when author does not exist`() = runTest {
        repository.removeAuthor("missing-author")

        assertTrue(dao.authors.isEmpty())
    }

    @Test
    fun `observeAllAuthors returns mapped authors`() = runTest {
        val author = createAuthor()
        dao.authors[author.id] = author.toEntityForTest()

        val result = repository.observeAllAuthors().first()

        assertEquals(listOf(author), result)
    }

    @Test
    fun `searchAuthors returns matching authors`() = runTest {
        val author = createAuthor()
        dao.authors[author.id] = author.toEntityForTest()

        val result = repository.searchAuthors("Tolkien").first()

        assertEquals(listOf(author), result)
    }

    private fun createAuthor(
        id: String = "OL123A",
        name: String = "J. R. R. Tolkien"
    ) = Author(
        id = id,
        name = name,
        birthDate = "1892",
        deathDate = "1973",
        bio = "English writer",
        photoId = 123
    )

    private fun Author.toEntityForTest() = AuthorEntity(
        id = id,
        name = name,
        birthDate = birthDate,
        deathDate = deathDate,
        bio = bio,
        photoId = photoId
    )

    private fun AuthorEntity.toDomainForTest() = Author(
        id = id,
        name = name,
        birthDate = birthDate,
        deathDate = deathDate,
        bio = bio,
        photoId = photoId
    )

    private class FakeOpenLibraryApi : OpenLibraryApi {

        var authorResponse = AuthorDto()
        var authorException: Exception? = null
        var lastAuthorId: String? = null

        override suspend fun getAuthor(
            authorId: String
        ): AuthorDto {
            lastAuthorId = authorId
            authorException?.let { throw it }
            return authorResponse
        }

        override suspend fun searchBooks(
            query: String,
            page: Int,
            limit: Int
        ) = BookSearchResponseDto(docs = emptyList())

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
        ) = BookDetailsDto()

        override suspend fun getAuthorWorks(
            authorId: String,
            limit: Int
        ) = BookSearchResponseDto(docs = emptyList())
    }

    private class FakeAuthorDao : AuthorDao {

        val authors = mutableMapOf<String, AuthorEntity>()

        override suspend fun insertAuthor(author: AuthorEntity) {
            authors[author.id] = author
        }

        override suspend fun insertAuthors(
            authors: List<AuthorEntity>
        ) {
            authors.forEach { this.authors[it.id] = it }
        }

        override suspend fun updateAuthor(author: AuthorEntity) {
            authors[author.id] = author
        }

        override suspend fun deleteAuthor(author: AuthorEntity) {
            authors.remove(author.id)
        }

        override suspend fun getAuthorById(
            authorId: String
        ): AuthorEntity? = authors[authorId]

        override fun observeAuthorById(
            authorId: String
        ): Flow<AuthorEntity?> = flowOf(authors[authorId])

        override fun observeAllAuthors(): Flow<List<AuthorEntity>> =
            flowOf(authors.values.sortedBy { it.name })

        override fun searchAuthors(
            query: String
        ): Flow<List<AuthorEntity>> =
            flowOf(
                authors.values.filter {
                    it.name.contains(query, ignoreCase = true)
                }.sortedBy { it.name }
            )

        override suspend fun deleteAllAuthors() {
            authors.clear()
        }
    }
}