package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.usecase.book.SearchBooksUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SearchBooksUseCaseTest {
    @Test
    fun `deve retornar livros quando buscar for realizada` () = runTest {
        //Given
        val expectedBooks = listOf(
            Book(
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
        )
        val repository = FakeBookRepository()

        repository.searchResult =
            Result.success(expectedBooks)

        val useCase = SearchBooksUseCase(repository)

        //When
        val result = useCase("Harry Potter")

        //Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(expectedBooks,result.getOrNull())
    }

    @Test
    fun `deve retornar lista vazia quando buscar estiver vazia` () = runTest {
        //Given
        val repository = FakeBookRepository()

        val useCase = SearchBooksUseCase(repository)

        //When
        val result = useCase("")

        //Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(
            result.getOrNull().isNullOrEmpty()
        )
    }

    @Test
    fun `deve retornar falha quando repository falhar` () = runTest {
        //Given
        val exception = RuntimeException("Erro de rede")
        val repository = FakeBookRepository()

        repository.searchResult =
            Result.failure(exception)

        val useCase = SearchBooksUseCase(repository)

        //When
        val result = useCase("Harry Potter")

        //Then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(
            exception,
            result.exceptionOrNull()
        )
    }
}