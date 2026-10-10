package com.example.biblioteca.domain.repository

import com.example.biblioteca.domain.model.Author
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    suspend fun getAuthor(authorId: String): Result<Author>

    suspend fun saveAuthor(author: Author)

    suspend fun getSavedAuthor(authorId: String): Author?

    suspend fun removeAuthor(authorId: String)

    fun observeAllAuthors(): Flow<List<Author>>

    fun searchAuthors(query: String): Flow<List<Author>>
}