package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.usecase.book.GetSavedBookUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetSavedBookUseCaseTest {
    @Test
    fun `deve retornar o livro salvo pelo id` () = runTest {
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
        val exceptedLibraryBook = LibraryBook(
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


        repository.addLibraryBook(exceptedLibraryBook)

        val useCase = GetSavedBookUseCase(repository)

        //When
        val result = useCase(book.id)

        //Then
        Assert.assertEquals(exceptedLibraryBook,result)
    }

    @Test
    fun `deve retornar null quando o livro noo estiver salvo` () = runTest {
        //Given
        val repository = FakeBookRepository()
        val useCase = GetSavedBookUseCase(repository)

        //When
        val result = useCase("OL999W")

        //Then
        Assert.assertNull(result)
    }

    @Test
    fun `deve retornar null quando o id estiver vazio` () = runTest {
        //Given
        val repository = FakeBookRepository()
        val useCase = GetSavedBookUseCase(repository)

        //When
        val result = useCase(" ")

        //Then
        Assert.assertNull(result)
    }
}