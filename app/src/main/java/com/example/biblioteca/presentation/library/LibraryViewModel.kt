package com.example.biblioteca.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.domain.usecase.book.GetSavedBooksUseCase
import com.example.biblioteca.domain.usecase.book.RemoveBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getSavedBooksUseCase: GetSavedBooksUseCase,
    private val removeBookUseCase: RemoveBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        observeSavedBooks()
    }

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    private fun observeSavedBooks() {
        viewModelScope.launch {
            getSavedBooksUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erro ao carregar a biblioteca."
                    )
                }
                .collect { books ->
                    _uiState.value = _uiState.value.copy(
                        books = books,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun removeBook(bookId: String) {
        viewModelScope.launch {
            try {
                removeBookUseCase(bookId)
                _uiState.value = _uiState.value.copy(
                    message = "Livro removido da biblioteca."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Erro ao remover o livro."
                )
            }
        }
    }
}