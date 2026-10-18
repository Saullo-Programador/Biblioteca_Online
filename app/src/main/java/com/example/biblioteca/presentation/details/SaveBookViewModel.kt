package com.example.biblioteca.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.repository.BookRepository
import com.example.biblioteca.domain.usecase.book.SaveBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveBookViewModel @Inject constructor(
    private val saveBookUseCase: SaveBookUseCase,
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveBookUiState())
    val uiState = _uiState.asStateFlow()

    fun saveBook(book: Book) {
        if (book.id.isBlank()) {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    error = "O ID do livro não pode estar vazio.",
                    message = null
                )
            }
            return
        }

        if (_uiState.value.isSaving) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    error = null,
                    message = null
                )
            }

            try {
                val existingBook = repository.getSavedBook(book.id)

                if (existingBook != null) {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSaved = true,
                            message = "Este livro já está na sua biblioteca."
                        )
                    }
                    return@launch
                }

                saveBookUseCase(book)

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        error = null,
                        message = "Livro salvo na sua biblioteca."
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = exception.message
                            ?: "Não foi possível salvar o livro.",
                        message = null
                    )
                }
            }
        }
    }
}
