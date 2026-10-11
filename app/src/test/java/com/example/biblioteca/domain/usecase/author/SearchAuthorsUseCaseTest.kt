package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SearchAuthorsUseCaseTest {
    @Test
    fun `deve retornar autores quando busca for realizada` () = runTest {
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

        repository.authors[expectedAuthor.id] = expectedAuthor

        val useCase = SearchAuthorsUseCase(repository)

        //When
        val result = useCase("Tolkien").first()

        //Then
        Assert.assertEquals(listOf(expectedAuthor), result)
    }

    @Test
    fun `deve retornar lista vazia quando nenhum autor for encontrado`() = runTest {
        // Given
        val repository = FakeAuthorRepository()
        val useCase = SearchAuthorsUseCase(repository)

        // When
        val result = useCase("Autor inexistente").first()

        // Then
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `deve retornar falha quando repository falhar`() = runTest {
        // Given
        val repository = FakeAuthorRepository()
        repository.searchException = RuntimeException("Erro ao buscar autores")

        val useCase = SearchAuthorsUseCase(repository)

        // When
        val result = runCatching {
            useCase("Tolkien").first()
        }

        // Then
        Assert.assertTrue(result.isFailure)
    }
}