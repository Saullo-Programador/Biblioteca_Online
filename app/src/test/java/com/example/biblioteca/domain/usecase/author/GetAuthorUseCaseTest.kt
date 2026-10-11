package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetAuthorUseCaseTest {
    @Test
    fun `deve retornar o autor pelo ID` () = runTest {
        //Given
        val expectedAuthor = Author(
            id = "OL123A",
            name = "J. R. R. Tolkien",
            birthDate = "3 January 1892",
            deathDate = "2 September 1973",
            bio = "English writer and philologist, best known for The Lord of the Rings and The Hobbit.",
            photoId = 123
        )

        val repository = FakeAuthorRepository()
        repository.authorResult =
            Result.success(expectedAuthor)

        val useCase = GetAuthorUseCase(repository)

        //When
        val result = useCase("OL123A")

        //Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(
            expectedAuthor,
            result.getOrNull()
        )
    }

    @Test
    fun `deve retornar falha quando o id estiver vazio` () = runTest {
        //Given
        val repository = FakeAuthorRepository()

        val useCase = GetAuthorUseCase(repository)

        //When
        val result = useCase("")

        //Then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}