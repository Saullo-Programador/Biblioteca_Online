package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class GetSavedBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String
    ): LibraryBook? {

        if(bookId.isBlank()){
            return null
        }

        return repository.getSavedBook(bookId)

    }
}