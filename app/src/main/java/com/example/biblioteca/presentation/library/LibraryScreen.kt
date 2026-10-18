package com.example.biblioteca.presentation.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.ui.components.BookCard
import com.example.biblioteca.ui.components.CustomInput
import com.example.biblioteca.ui.theme.BibliotecaTheme

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LibraryScreenContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onRemoveBook = viewModel::removeBook,
        onBookClick = onBookClick
    )
}

@Composable
fun LibraryScreenContent(
    state: LibraryUiState,
    onQueryChange: (String) -> Unit = {},
    onRemoveBook: (String) -> Unit = {},
    onBookClick: (String) -> Unit = {}
) {
    var bookToRemove by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Minha biblioteca",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomInput(
            value = state.query,
            onValueChange = onQueryChange,
            label = "Buscar por título ou autor",
            placeholder = "Digite para buscar...",
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        state.message?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.filteredBooks.isEmpty() -> {
                Text(
                    text = if (state.query.isBlank()) {
                        "Sua biblioteca está vazia. Adicione livros pela busca."
                    } else {
                        "Nenhum livro encontrado para essa pesquisa."
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.filteredBooks,
                        key = { it.book.id }
                    ) { libraryBook ->
                        Column {
                            BookCard(
                                libraryBook = libraryBook,
                                modifier = Modifier.fillMaxWidth()
                            )

                            TextButton(
                                onClick = {
                                    bookToRemove = libraryBook.book.id
                                }
                            ) {
                                Text(
                                    text = "Remover da biblioteca",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    bookToRemove?.let { bookId ->
        AlertDialog(
            onDismissRequest = {
                bookToRemove = null
            },
            title = {
                Text("Remover livro")
            },
            text = {
                Text("Deseja realmente remover este livro da sua biblioteca?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRemoveBook(bookId)
                        bookToRemove = null
                    }
                ) {
                    Text("Remover")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        bookToRemove = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private val sampleLibraryBooks = listOf(
    LibraryBook(
        book = Book(
            id = "1",
            title = "O Senhor dos Anéis",
            authors = listOf("J.R.R. Tolkien"),
            coverId = null,
            firstPublishYear = 1954,
            isbn = "9780007525546",
            publisher = "Allen & Unwin",
            numberOfPages = 1200,
            subjects = listOf("Fantasy")
        ),
        status = ReadingStatus.READING,
        currentPage = 350
    ),
    LibraryBook(
        book = Book(
            id = "2",
            title = "1984",
            authors = listOf("George Orwell"),
            coverId = null,
            firstPublishYear = 1949,
            isbn = "9780451524935",
            publisher = "Secker & Warburg",
            numberOfPages = 328,
            subjects = listOf("Dystopian")
        ),
        status = ReadingStatus.COMPLETED
    )
)

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    BibliotecaTheme {
        LibraryScreenContent(
            state = LibraryUiState(
                books = sampleLibraryBooks,
                isLoading = false
            )
        )
    }
}