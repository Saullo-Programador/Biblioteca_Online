package com.example.biblioteca.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.domain.usecase.book.GetBookDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadDetails(bookId: String) {
        if (bookId.isBlank()){
            _uiState.update {
                it.copy(
                    bookDetails = null,
                    isLoading = false,
                    error = "O ID do livro não pode estar vazio."
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {

                val result = getBookDetailsUseCase(bookId)

                result.fold(
                    onSuccess = { details ->
                        _uiState.update {
                            it.copy(
                                bookDetails = details,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                bookDetails = null,
                                isLoading = false,
                                error = exception.message
                                    ?: "Não foi possível carregar os detalhes do livro."
                            )
                        }
                    }
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception){
                _uiState.update {
                    it.copy(
                        bookDetails = null,
                        isLoading = false,
                        error = exception.message
                            ?: "Ocorreu um erro inesperado."
                    )
                }
            }
        }
    }
}