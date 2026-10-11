package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.usecase.book.SaveBookUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SaveBookUseCaseTest {
    @Test
    fun `deve salvar livro no repository` () = runTest {
        //Given
        val repository = FakeBookRepository()

        val useCase = SaveBookUseCase(repository)

        val book = Book(
            id = "OL123W",
            title = "Harry Potter",
            authors = listOf("J. K. Rowling"),
            coverId = 123,
            firstPublishYear = 1997,
            isbn = "9780747532743",
            publisher = "Bloomsbury",
            numberOfPages = 320,
            subjects = listOf("Fantasy")
        )

        //When
        useCase(book)

        //Then
        Assert.assertEquals(
            book,
            repository.lastSavedBook
        )
    }
}