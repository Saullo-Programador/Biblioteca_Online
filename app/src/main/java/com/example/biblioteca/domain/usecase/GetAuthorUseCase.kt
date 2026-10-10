package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import javax.inject.Inject

class GetAuthorUseCase @Inject constructor(
    private val repository: AuthorRepository
) {
    suspend operator fun invoke(
        authorId: String
    ): Result<Author> {
        if (authorId.isBlank()){
            return Result.failure(
                IllegalArgumentException("O ID do Author não pode estar vazio.")
            )
        }
        return repository.getAuthor(authorId)
    }
}