package com.example.biblioteca.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.domain.usecase.author.SearchAuthorsUseCase
import com.example.biblioteca.domain.usecase.book.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                error = null
            )
        }
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    books = emptyList(),
                    authors = emptyList(),
                    isLoading = false,
                    error = null
                )
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)
            performSearch(query.trim())
        }
    }


    fun searchAll() {
        val query = _uiState.value.query.trim()

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    books = emptyList(),
                    authors = emptyList(),
                    isLoading = false,
                    error = "Digite o nome de um livro ou autor para pesquisar."
                )
            }
            return
        }
        searchJob = viewModelScope.launch {
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String){
        _uiState.update{
            it.copy(
                isLoading = true,
                error = null,
                books = emptyList(),
                authors = emptyList()
            )
        }
        try {
            val (booksResult, authorsResult) = coroutineScope {
                val booksDeferred = async {
                    runCatching {
                        searchBooksUseCase(query).getOrThrow()
                    }
                }
                val authorsDeprecated = async {
                    runCatching {
                        searchAuthorsUseCase(query).first()
                    }
                }

                booksDeferred.await() to authorsDeprecated.await()
            }

            val books = booksResult.getOrDefault(emptyList())
            val authors = authorsResult.getOrDefault(emptyList())

            val errorMessage = when {
                booksResult.isFailure && authorsResult.isFailure ->
                    "Não foi possível pesquisar livros nem autores."

                booksResult.isFailure ->
                    "Não foi possível pesquisar livros."

                authorsResult.isFailure ->
                    "Não foi possível pesquisar autores."

                books.isEmpty() && authors.isEmpty() ->
                    "Nenhum livro ou autor encontrado."

                else -> null
            }

            _uiState.update {
                it.copy(
                    books = books,
                    authors = authors,
                    isLoading = false,
                    error = errorMessage
                )
            }
        }catch (exception: CancellationException){
            throw exception

        } catch (exception: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = exception.message
                        ?: "Ocorreu um erro inesperado"
                )
            }
        }
    }
}