package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.usecase.book.UpdateNotesUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateNotesUseCaseTest {

    @Test
    fun `deve salvar anotacao do livro`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateNotesUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            notes = "Livro muito interessante"
        )

        // Then
        Assert.assertEquals(
            "Livro muito interessante",
            repository.lastUpdatedNotes
        )
    }

    @Test
    fun `deve remover espacos extras da anotacao`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateNotesUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            notes = "   Minha anotacao   "
        )

        // Then
        Assert.assertEquals(
            "Minha anotacao",
            repository.lastUpdatedNotes
        )
    }
}