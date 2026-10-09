package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateReadingStatusUseCaseTest {

    @Test
    fun `deve atualizar status do livro`() = runTest {

        // Given
        val repository = FakeBookRepository()
        val useCase = UpdateReadingStatusUseCase(repository)

        // When
        useCase(
            bookId = "OL123W",
            status = ReadingStatus.READING
        )

        // Then
        Assert.assertEquals(
            ReadingStatus.READING,
            repository.lastUpdatedStatus
        )

        Assert.assertEquals(
            "OL123W",
            repository.lastUpdatedBookId
        )

    }
}