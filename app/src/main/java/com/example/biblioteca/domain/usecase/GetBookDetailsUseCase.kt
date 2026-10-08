package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String
    ): Result<BookDetails>{
        if(bookId.isBlank()){
            return Result.failure(
                IllegalArgumentException("O ID do livro não pode estar vazio.")
            )
        }
        return repository.getBookDetails(
            bookId = bookId
        )
    }
}