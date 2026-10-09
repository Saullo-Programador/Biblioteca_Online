package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSavedBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(
        query: String
    ): Flow<List<LibraryBook>>{
        if (query.isBlank()){
            return repository.observeSavedBooks()
        }
        return repository.searchSaveBooks(
            query.trim()
        )
    }
}