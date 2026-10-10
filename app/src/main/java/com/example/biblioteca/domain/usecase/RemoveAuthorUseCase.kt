package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.repository.AuthorRepository
import javax.inject.Inject

class RemoveAuthorUseCase @Inject constructor(
    private val repository: AuthorRepository
) {
    suspend operator fun invoke(
        authorId: String
    ){
        if (authorId.isBlank()) return
        repository.removeAuthor(authorId)
    }
}