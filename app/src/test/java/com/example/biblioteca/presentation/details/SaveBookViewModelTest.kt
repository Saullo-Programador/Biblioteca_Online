package com.example.biblioteca.presentation.details

import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.usecase.book.SaveBookUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveBookViewModelTest {

    private lateinit var repository: FakeBookRepository
    private lateinit var viewModel: SaveBookViewModel

    private val book = Book(
        id = "/works/OL123W",
        title = "Clean Architecture",
        authors = listOf("Robert C. Martin"),
        coverId = 12345,
        firstPublishYear = 2017,
        isbn = "9780134494166",
        publisher = "Prentice Hall",
        numberOfPages = 432,
        subjects = listOf("Software", "Architecture")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        repository = FakeBookRepository()
        viewModel = SaveBookViewModel(
            saveBookUseCase = SaveBookUseCase(repository),
            repository = repository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveBook with blank id should show validation error`() = runTest {
        viewModel.saveBook(book.copy(id = ""))
        advanceUntilIdle()

        assertEquals(
            "O ID do livro não pode estar vazio.",
            viewModel.uiState.value.error
        )
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveBook should save book successfully`() = runTest {
        viewModel.saveBook(book)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSaved)
        assertEquals(
            "Livro salvo na sua biblioteca.",
            viewModel.uiState.value.message
        )
        assertEquals(book, repository.lastSavedBook)
    }

    @Test
    fun `saveBook should not save duplicate book`() = runTest {
        repository.saveBook(book)

        viewModel.saveBook(book)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSaved)
        assertEquals(
            "Este livro já está na sua biblioteca.",
            viewModel.uiState.value.message
        )
    }
}
