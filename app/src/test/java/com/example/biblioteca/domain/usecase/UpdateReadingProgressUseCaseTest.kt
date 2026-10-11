package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.usecase.book.UpdateReadingProgressUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateReadingProgressUseCaseTest {

    @Test
    fun `deve atualizar pagina atual do livro`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateReadingProgressUseCase(repository)

        val bookId = "OL123W"
        val currentPage = 150

        // When
        useCase(
            bookId = bookId,
            currentPage = currentPage
        )

        // Then
        Assert.assertEquals(
            bookId,
            repository.lastUpdatedBookId
        )

        Assert.assertEquals(
            currentPage,
            repository.lastUpdatedPage
        )
    }

    @Test
    fun `nao deve atualizar quando pagina for negativa`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateReadingProgressUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            currentPage = -1
        )

        // Then
        Assert.assertEquals(
            null,
            repository.lastUpdatedPage
        )
    }
}