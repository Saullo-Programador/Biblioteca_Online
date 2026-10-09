package com.example.biblioteca.domain.usecase

import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateRatingUseCaseTest {

    @Test
    fun `deve atualizar avaliacao do livro`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateRatingUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            rating = 4.5f
        )

        // Then
        Assert.assertEquals(
            4.5f,
            repository.lastUpdatedRating
        )
    }

    @Test
    fun `nao deve aceitar avaliacao maior que cinco`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateRatingUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            rating = 6f
        )

        // Then
        Assert.assertEquals(
            null,
            repository.lastUpdatedRating
        )
    }
}