package com.example.biblioteca.domain.usecase.book

import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class UpdateReadingProgressUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String,
        currentPage: Int
    ){
        if (bookId.isBlank()) return

        if (currentPage < 0) return

        repository.updateReadingProgress(
            bookId = bookId,
            currentPage = currentPage
        )

    }
}