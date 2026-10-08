package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class UpdateReadingStatusUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String,
        status: ReadingStatus
    ){
        if (bookId.isBlank()) return

        repository.updateReadingStatus(
            bookId = bookId,
            status = status
        )
    }
}