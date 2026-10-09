package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SearchSavedBooksUseCaseTest {

    @Test
    fun `deve retornar livros salvos quando busca for realizada` () = runTest {
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
            status = ReadingStatus.READING,
            currentPage = 150,
            rating = 4.5f,
            notes = "Minha anotação",
            startedAt = 1_760_000_000_000L,
            finishedAt = null,
            savedAt = 1_759_000_000_000L
        )
        val repository = FakeBookRepository()

        repository.addLibraryBook(libraryBook)

        val useCase = SearchSavedBooksUseCase(repository)

        //When
        val result = useCase("Harry Potter").first()

        //Then
        Assert.assertEquals(listOf(libraryBook),result)

    }
}