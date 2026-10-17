package com.example.biblioteca.presentation.reading

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.domain.repository.AuthorRepository
import com.example.biblioteca.domain.repository.BookRepository
import com.example.biblioteca.domain.usecase.book.GetSavedBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val getSavedBooksUseCase: GetSavedBooksUseCase,
    private val repository: BookRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ReadingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeReadingBooks()
    }

    fun observeReadingBooks(){
        viewModelScope.launch {
            getSavedBooksUseCase()
                .map { books ->
                    books.filter { it.status == ReadingStatus.READING }
                }
                .onStart {
                    _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
                .catch { exception ->
                    if (exception is CancellationException) throw exception

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Não foi possível carregar os livros em leitura."
                        )
                    }
                }
                .collect { books ->
                    _uiState.update {
                        it.copy(
                            books = books,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun updateProgress(bookId: String, currentPage: Int){
        if (bookId.isBlank()){
            showError("O ID do livro não pode estar vazio.")
            return
        }
        if (currentPage < 0 ){
            showError("A página atual não pode ser negativa.")
            return
        }

        viewModelScope.launch {
            try {
                repository.updateReadingProgress(bookId, currentPage)
                _uiState.update {
                    it.copy(
                        error = null,
                        message = "Progresso atualizado."
                    )
                }
            } catch (exception: CancellationException){
                throw exception
            } catch (exception: Exception){
                showError(
                    exception.message ?: "Não foi possível atualizar o progresso."
                )
            }
        }
    }

    fun updateStatus(bookId: String, status: ReadingStatus){
        if (bookId.isBlank()) {
            showError("O ID do livro não pode estar vazio.")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateReadingStatus(bookId, status)
                _uiState.update {
                    it.copy(
                        error = null,
                        message = "Status de leitura atualizado."
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                showError(
                    exception.message ?: "Não foi possível atualizar o status."
                )
            }
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(error = message, message = null)
        }
    }
}