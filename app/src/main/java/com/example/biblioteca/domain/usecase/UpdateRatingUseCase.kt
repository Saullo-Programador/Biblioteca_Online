package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.repository.BookRepository
import javax.inject.Inject

class UpdateRatingUseCase @Inject constructor(
    private val repository: BookRepository
){

    suspend operator fun invoke(
        bookId: String,
        rating: Float?
    ){
        if (bookId.isBlank()) return

        if (rating != null && rating !in 0f..5f){
            return
        }

        repository.updateRating(
            bookId = bookId,
            rating = rating
        )
    }
}