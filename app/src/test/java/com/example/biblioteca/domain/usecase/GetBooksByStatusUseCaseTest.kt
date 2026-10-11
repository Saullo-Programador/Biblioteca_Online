package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.usecase.book.GetBooksByStatusUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetBooksByStatusUseCaseTest {

    @Test
    fun `deve retornar os livros com o mesmo status` () = runTest {
        //Given
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

        val useCase = GetBooksByStatusUseCase(repository)

        //When
        val result = useCase(ReadingStatus.WANT_TO_READ).first()

        //Then
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(libraryBook2, result.first())
        Assert.assertTrue( result.all { it.status == ReadingStatus.WANT_TO_READ } )
    }

    @Test
    fun `deve retornar lista vazia quando nenhum livro possuir o status` () = runTest {
        //Given
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

        val libraryBook = LibraryBook(
            book = book,
            status = ReadingStatus.WANT_TO_READ,
            currentPage = 0,
            rating = null,
            notes = null,
            startedAt = null,
            finishedAt = null,
            savedAt = 1_758_000_000_000L
        )

        val repository = FakeBookRepository()
        repository.addLibraryBook(libraryBook)

        val useCase = GetBooksByStatusUseCase(repository)

        //When
        val result = useCase(ReadingStatus.COMPLETED).first()

        //Then
        Assert.assertTrue(result.isEmpty())
    }
}