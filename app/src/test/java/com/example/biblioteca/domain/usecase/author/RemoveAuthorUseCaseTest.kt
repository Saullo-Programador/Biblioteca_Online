package com.example.biblioteca.domain.usecase.author

import com.example.biblioteca.fake.FakeAuthorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RemoveAuthorUseCaseTest {
    @Test
    fun `deve remover o autor pelo id` () = runTest {
        //Given
        val repository = FakeAuthorRepository()
        val useCase = RemoveAuthorUseCase(repository)

        val authorId = "OL123A"

        //When
        useCase(authorId)

        //Then
        Assert.assertEquals(
            authorId,
            repository.lastRemovedAuthorId
        )
    }
}