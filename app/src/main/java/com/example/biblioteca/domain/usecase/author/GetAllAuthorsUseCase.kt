package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAuthorsUseCase @Inject constructor(
    private val repository: AuthorRepository
) {
    operator fun invoke(): Flow<List<Author>> {
        return repository.observeAllAuthors()
    }
}