package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchAuthorsUseCase @Inject constructor(
    private val repository: AuthorRepository
) {
    operator fun invoke (
        query: String
    ): Flow<List<Author>> {
        if (query.isBlank()){
            return repository.observeAllAuthors()
        }
        return repository.searchAuthors(query.trim())
    }
}