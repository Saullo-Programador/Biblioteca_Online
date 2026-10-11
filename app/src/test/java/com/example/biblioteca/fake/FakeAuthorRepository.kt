package com.example.biblioteca.fake

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthorRepository : AuthorRepository {

    val authors = mutableMapOf<String, Author>()

    var authorResult: Result<Author>? = null
    var lastRequestedAuthorId: String? = null
    var lastSavedAuthor: Author? = null
    var lastRemovedAuthorId: String? = null

    var searchException: Exception? = null

    override suspend fun getAuthor(authorId: String): Result<Author> {
        lastRequestedAuthorId = authorId

        return authorResult
            ?: authors[authorId]?.let { Result.success(it) }
            ?: Result.failure(
                NoSuchElementException("Author not found: $authorId")
            )
    }

    override suspend fun saveAuthor(author: Author) {
        lastSavedAuthor = author
        authors[author.id] = author
    }

    override suspend fun getSavedAuthor(authorId: String): Author? {
        return authors[authorId]
    }

    override suspend fun removeAuthor(authorId: String) {
        lastRemovedAuthorId = authorId
        authors.remove(authorId)
    }

    override fun observeAllAuthors(): Flow<List<Author>> {
        return flowOf(authors.values.toList())
    }

    override fun searchAuthors(query: String): Flow<List<Author>> {
        searchException?.let { throw it }

        val filteredAuthors = authors.values
            .filter { author ->
                author.name.contains(query, ignoreCase = true)
            }
            .sortedBy { it.name }

        return flowOf(filteredAuthors)
    }
}
