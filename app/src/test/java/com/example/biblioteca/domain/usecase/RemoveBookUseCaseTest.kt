package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.usecase.book.RemoveBookUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RemoveBookUseCaseTest {
    @Test
    fun `deve remover livro pelo id` () = runTest {
        //Given
        val repository = FakeBookRepository()

        val useCase = RemoveBookUseCase(repository)

        val bookId = "OL123W"

        //When
        useCase(bookId)

        //Then
        Assert.assertEquals(
            bookId,
            repository.lastRemovedBookId
        )
    }
}