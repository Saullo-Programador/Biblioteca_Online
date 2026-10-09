package com.example.biblioteca.domain.usecase

import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetBookDetailsUseCaseTest {
    @Test
    fun `deve retornar detalhes do livro` () = runTest {
        //Given
        val exceptedBook = BookDetails(
            id = "OL123W",
            title = "Harry Potter",
            description = "A wizarding story.",
            firstPublishDate = "1997",
            coverId = 123,
            subjects = listOf("Fantasy"),
            authorIds = listOf("OL456A")
        )

        val repository = FakeBookRepository()
        repository.bookDetailsResult =
            Result.success(exceptedBook)

        val useCase = GetBookDetailsUseCase(repository)

        //When
        val result = useCase("0L123W")

        //Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(
            exceptedBook,
            result.getOrNull()
        )
    }

    @Test
    fun `deve retornar falha quando o id estiver vazio` () = runTest {
        //Given
        val repository = FakeBookRepository()

        val useCase = GetBookDetailsUseCase(repository)

        //When
        val result = useCase("")

        //Then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(
            result.exceptionOrNull() is IllegalArgumentException
        )
    }
}