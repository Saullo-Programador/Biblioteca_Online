package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.usecase.book.GetSavedBooksUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetSavedBooksUseCaseTest {

    @Test
    fun `deve retornar todos os livros salvos`() = runTest {
        // Given
        val book1 = Book(
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

        val book2 = Book(
            id = "OL456W",
            title = "Clean Code",
            authors = listOf("Robert C. Martin"),
            coverId = 456,
            firstPublishYear = 2008,
            isbn = "9780132350884",
            publisher = "Prentice Hall",
            numberOfPages = 464,
            subjects = listOf("Programming")
        )

        val libraryBook1 = LibraryBook(
            book = book1,
            status = ReadingStatus.READING,
            currentPage = 150,
            rating = 4.5f,
            notes = "Minha anotação",
            startedAt = 1_760_000_000_000L,
            finishedAt = null,
            savedAt = 1_759_000_000_000L
        )

        val libraryBook2 = LibraryBook(
            book = book2,
            status = ReadingStatus.WANT_TO_READ,
            currentPage = 0,
            rating = null,
            notes = null,
            startedAt = null,
            finishedAt = null,
            savedAt = 1_758_000_000_000L
        )

        val repository = FakeBookRepository()

        repository.addLibraryBook(libraryBook1)
        repository.addLibraryBook(libraryBook2)

        val useCase = GetSavedBooksUseCase(repository)

        //When
        val result = useCase().first()

        //Then
        Assert.assertEquals(2,result.size)
        Assert.assertTrue(result.contains(libraryBook1))
        Assert.assertTrue(result.contains(libraryBook2))
    }

    @Test
    fun `deve retornar lista vazia quando nao houver livros salvos` () = runTest {
        //Given
        val repository = FakeBookRepository()
        val useCase = GetSavedBooksUseCase(repository)

        //When
        val result = useCase().first()

        //Then
        Assert.assertTrue(result.isEmpty())
    }
}