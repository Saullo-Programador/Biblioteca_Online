package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.repository.AuthorRepository
import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import javax.inject.Inject

class GetAllAuthorsUseCaseTest {
    @Test
    fun `deve retornar todos os autores cadastrados` () = runTest {
        //Given
        val expectedAuthors = listOf(
            Author(
                id = "OL123A",
                name = "J. R. R. Tolkien",
                birthDate = "3 January 1892",
                deathDate = "2 September 1973",
                bio = "English writer and philologist, best known for The Lord of the Rings and The Hobbit.",
                photoId = 123
            ),
            Author(
                id = "OL456A",
                name = "George Orwell",
                birthDate = "25 June 1903",
                deathDate = "21 January 1950",
                bio = "English novelist, essayist and journalist.",
                photoId = 456
            )
        )

        val repository = FakeAuthorRepository()

        expectedAuthors.forEach { author ->
            repository.authors[author.id] = author
        }

        val useCase = GetAllAuthorsUseCase(repository)

        //When
        val result = useCase().first()

        //Then
        Assert.assertEquals(expectedAuthors, result)
    }

    @Test
    fun `deve retornar lista vazia quando nao houver autores cadastrados`() = runTest {
        //Given
        val repository = FakeAuthorRepository()
        val useCase = GetAllAuthorsUseCase(repository)
        //When
        val result = useCase().first()
        //Then
        Assert.assertTrue(result.isEmpty())
    }
}