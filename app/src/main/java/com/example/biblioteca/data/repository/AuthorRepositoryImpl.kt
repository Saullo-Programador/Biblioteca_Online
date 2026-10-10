package com.example.biblioteca.data.repository

import com.example.biblioteca.data.local.dao.AuthorDao
import com.example.biblioteca.data.mapper.toDomain
import com.example.biblioteca.data.mapper.toEntity
import com.example.biblioteca.data.remote.api.OpenLibraryApi
import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthorRepositoryImpl @Inject constructor(
    private val api: OpenLibraryApi,
    private val authorDao: AuthorDao
): AuthorRepository {
    override suspend fun getAuthor(authorId: String): Result<Author> {
        return try {
            val normalizedId = authorId
                .removePrefix("/authors/")

            val response = api.getAuthor(
                authorId = normalizedId
            )
            Result.success(response.toDomain())
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    override suspend fun saveAuthor(author: Author) {
        authorDao.insertAuthor(author.toEntity())
    }

    override suspend fun getSavedAuthor(authorId: String): Author? {
        val normalizedId = authorId
            .removePrefix("/authors/")

        return authorDao.getAuthorById(normalizedId)?.toDomain()
    }

    override suspend fun removeAuthor(authorId: String) {
        val normalizedId = authorId
            .removePrefix("/authors/")
        val author = authorDao.getAuthorById(authorId)

        if (author != null){
            authorDao.deleteAuthor(author)
        }
    }

    override fun observeAllAuthors(): Flow<List<Author>> {
        return authorDao.observeAllAuthors().map { authors ->
            authors.map { entity -> entity.toDomain() }
        }
    }

    override fun searchAuthors(query: String): Flow<List<Author>> {
        return authorDao.searchAuthors(query).map { authors ->
            authors.map { entity -> entity.toDomain() }
        }
    }

}