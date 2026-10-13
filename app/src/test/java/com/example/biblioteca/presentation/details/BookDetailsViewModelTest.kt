package com.example.biblioteca.presentation.details

import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.usecase.book.GetBookDetailsUseCase
import com.example.biblioteca.fake.FakeBookRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailsViewModelTest {
    private lateinit var fakeRepository: FakeBookRepository
    private lateinit var viewModel: BookDetailsViewModel

    private val expectedDetails = BookDetails(
        id = "/works/OL123W",
        title = "Clean Architecture",
        description = "Um livro sobre arquitetura de software.",
        firstPublishDate = "2017",
        coverId = 12345,
        subjects = listOf("Software", "Architecture"),
        authorIds = listOf("OL456A")
    )

    @Before
    fun setup(){
        fakeRepository = FakeBookRepository()

        viewModel = BookDetailsViewModel(
            getBookDetailsUseCase = GetBookDetailsUseCase(fakeRepository)
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun `loadDetails with blank id should show validation error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        viewModel.loadDetails(" ")

        val state = viewModel.uiState.value

        Assert.assertEquals(
            "O ID do livro não pode estar vazio.",
            state.error
        )
        Assert.assertNull(state.bookDetails)
        Assert.assertFalse(state.isLoading)
    }

    @Test
    fun `loadDetails with valid id should load book details`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        fakeRepository.bookDetailsResult = Result.success(expectedDetails)

        viewModel.loadDetails(expectedDetails.id)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        Assert.assertEquals(expectedDetails, state.bookDetails)
        Assert.assertNull(state.error)
        Assert.assertFalse(state.isLoading)
    }

    @Test
    fun `loadDetails when repository fails should show error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        fakeRepository.bookDetailsResult =
            Result.failure(RuntimeException("Erro de conexão"))

        viewModel.loadDetails(expectedDetails.id)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        Assert.assertNull(state.bookDetails)
        Assert.assertEquals("Erro de conexão", state.error)
        Assert.assertFalse(state.isLoading)
    }

    @Test
    fun `loadDetails should set loading while request is running`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        val gate = CompletableDeferred<Unit>()
        fakeRepository.bookDetailsGate = gate
        fakeRepository.bookDetailsResult = Result.success(expectedDetails)

        viewModel.loadDetails(expectedDetails.id)

        testScheduler.runCurrent()

        Assert.assertTrue(viewModel.uiState.value.isLoading)

        gate.complete(Unit)
        advanceUntilIdle()

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertEquals(expectedDetails, viewModel.uiState.value.bookDetails)
    }

}