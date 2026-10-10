package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import javax.inject.Inject

class SaveAuthorUseCase @Inject constructor(
    private val repository: AuthorRepository
) {
    suspend operator fun invoke(
        author: Author
    ){
        repository.saveAuthor(author)
    }
}