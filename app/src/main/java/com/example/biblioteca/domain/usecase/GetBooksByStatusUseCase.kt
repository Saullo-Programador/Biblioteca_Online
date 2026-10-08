package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksByStatusUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(
        status: ReadingStatus
    ): Flow<List<Book>>{
        return repository.observeBooksByStatus(status)
    }
}