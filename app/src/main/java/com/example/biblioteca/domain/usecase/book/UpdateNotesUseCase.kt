package com.example.biblioteca.domain.usecase.book

import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class UpdateNotesUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String,
        notes: String?
    ){
        if (bookId.isBlank()) return

        repository.updateNotes(
            bookId = bookId,
            notes = notes?.trim()
        )
    }
}