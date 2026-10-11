package com.example.biblioteca.domain.usecase.book

import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class RemoveBookUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend operator fun invoke(
        bookId: String
    ){
        if (bookId.isBlank()) return
        repository.removeBook(bookId)
    }
}