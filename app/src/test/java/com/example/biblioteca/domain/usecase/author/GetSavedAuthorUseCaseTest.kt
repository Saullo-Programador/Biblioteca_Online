package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetSavedAuthorUseCaseTest {
    @Test
    fun `deve retornar o author salvo pelo id` () = runTest {
        //Given
        val author = Author(
            id = "OL123A",
            name = "J. R. R. Tolkien",
            birthDate = "3 January 1892",
            deathDate = "2 September 1973",
            bio = "English writer and philologist, best known for The Lord of the Rings and The Hobbit.",
            photoId = 123
        )

        val repository = FakeAuthorRepository()
        repository.authors[author.id] = author
        val useCase = GetSavedAuthorUseCase(repository)

        //When
        val result = useCase(author.id)

        //Then
        Assert.assertEquals(author, result)
    }

    @Test
    fun `deve retornar null quando o author nao tiver salvo` () = runTest {
        //Given
        val repository = FakeAuthorRepository()
        val useCase = GetSavedAuthorUseCase(repository)

        //When
        val result = useCase("OL999A")

        //Then
        Assert.assertNull(result)
    }

    @Test
    fun `deve retornar null quando o id estiver vazio` () = runTest {
        //Given
        val repository = FakeAuthorRepository()
        val useCase = GetSavedAuthorUseCase(repository)

        //When
        val result = useCase(" ")

        //Then
        Assert.assertNull(result)
    }
}