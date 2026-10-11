package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SaveAuthorUseCaseTest {
    @Test
    fun `deve salvar o autor no repository` () = runTest {
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
        val useCase = SaveAuthorUseCase(repository)

        //When
        useCase(author)

        //Then
        Assert.assertEquals(
            author,
            repository.lastSavedAuthor
        )
    }
}