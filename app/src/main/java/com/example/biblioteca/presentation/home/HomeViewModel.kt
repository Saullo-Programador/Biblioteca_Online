package com.example.biblioteca.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.domain.usecase.book.GetSavedBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSavedBooksUseCase: GetSavedBooksUseCase
): ViewModel () {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeBooks()
    }

    private fun observeBooks(){
        viewModelScope.launch {
            getSavedBooksUseCase()
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null
                    )
                }
                .catch { exception ->
                    if (exception is CancellationException) throw exception

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                            ?: "Não foi possível carregar sua biblioteca."
                    )
                }
                .collect { books ->
                    _uiState.value = HomeUiState(
                        books = books,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}