package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Book>> {
        if (query.isBlank()){
            return Result.success(emptyList())
        }

        return repository.searchBooks(
            query = query.trim(),
            page = page,
            limit = limit
        )
    }
}