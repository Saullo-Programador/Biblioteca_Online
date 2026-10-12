package com.example.biblioteca.presentation.search

import com.example.biblioteca.domain.model.Author
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.usecase.author.SearchAuthorsUseCase
import com.example.biblioteca.domain.usecase.book.SearchBooksUseCase
import com.example.biblioteca.fake.FakeAuthorRepository
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var bookRepository: FakeBookRepository
    private lateinit var authorRepository: FakeAuthorRepository
    private lateinit var viewModel: SearchViewModel

    private val expectedBook = Book(
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

    private val expectedAuthor = Author(
        id = "OL123A",
        name = "J. R. R. Tolkien",
        birthDate = "3 January 1892",
        deathDate = "2 September 1973",
        bio = "English writer and philologist.",
        photoId = 123
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        bookRepository = FakeBookRepository()
        authorRepository = FakeAuthorRepository()

        viewModel = SearchViewModel(
            searchBooksUseCase = SearchBooksUseCase(bookRepository),
            searchAuthorsUseCase = SearchAuthorsUseCase(authorRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `deve iniciar com estado vazio`() {
        // Given / When
        val result = viewModel.uiState.value

        // Then
        assertEquals("", result.query)
        assertTrue(result.books.isEmpty())
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertNull(result.error)
    }

    @Test
    fun `deve atualizar a consulta`() {
        // When
        viewModel.onQueryChange("Tolkien")

        // Then
        assertEquals("Tolkien", viewModel.uiState.value.query)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `deve pesquisar livros e autores com sucesso`() = runTest {
        // Given
        bookRepository.searchResult = Result.success(listOf(expectedBook))
        authorRepository.authors[expectedAuthor.id] = expectedAuthor

        viewModel.onQueryChange("Harry")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertEquals(listOf(expectedBook), result.books)
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertNull(result.error)
    }

    @Test
    fun `deve encontrar autores pela consulta`() = runTest {
        // Given
        authorRepository.authors[expectedAuthor.id] = expectedAuthor
        bookRepository.searchResult = Result.success(emptyList())

        viewModel.onQueryChange("Tolkien")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertTrue(result.books.isEmpty())
        assertEquals(listOf(expectedAuthor), result.authors)
        assertFalse(result.isLoading)
        assertNull(result.error)
    }

    @Test
    fun `deve exibir erro quando a consulta estiver vazia`() {
        // When
        viewModel.searchAll()

        // Then
        val result = viewModel.uiState.value

        assertTrue(result.books.isEmpty())
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertEquals(
            "Digite o nome de um livro ou autor para pesquisar.",
            result.error
        )
    }

    @Test
    fun `deve exibir erro quando nenhum resultado for encontrado`() = runTest {
        // Given
        bookRepository.searchResult = Result.success(emptyList())

        viewModel.onQueryChange("Inexistente")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertTrue(result.books.isEmpty())
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertEquals(
            "Nenhum livro ou autor encontrado.",
            result.error
        )
    }

    @Test
    fun `deve manter autores quando a pesquisa de livros falhar`() = runTest {
        // Given
        bookRepository.searchResult =
            Result.failure(RuntimeException("Erro de rede"))

        authorRepository.authors[expectedAuthor.id] = expectedAuthor

        viewModel.onQueryChange("Tolkien")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertTrue(result.books.isEmpty())
        assertEquals(listOf(expectedAuthor), result.authors)
        assertFalse(result.isLoading)
        assertEquals(
            "Não foi possível pesquisar livros.",
            result.error
        )
    }

    @Test
    fun `deve manter livros quando a pesquisa de autores falhar`() = runTest {
        // Given
        bookRepository.searchResult = Result.success(listOf(expectedBook))
        authorRepository.searchException =
            RuntimeException("Erro ao buscar autores")

        viewModel.onQueryChange("Harry Potter")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertEquals(listOf(expectedBook), result.books)
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertEquals(
            "Não foi possível pesquisar autores.",
            result.error
        )
    }

    @Test
    fun `deve exibir erro quando as duas pesquisas falharem`() = runTest {
        // Given
        bookRepository.searchResult =
            Result.failure(RuntimeException("Erro nos livros"))

        authorRepository.searchException =
            RuntimeException("Erro nos autores")

        viewModel.onQueryChange("Tolkien")

        // When
        viewModel.searchAll()
        advanceUntilIdle()

        // Then
        val result = viewModel.uiState.value

        assertTrue(result.books.isEmpty())
        assertTrue(result.authors.isEmpty())
        assertFalse(result.isLoading)
        assertEquals(
            "Não foi possível pesquisar livros nem autores.",
            result.error
        )
    }
}