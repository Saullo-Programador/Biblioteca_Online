package com.example.biblioteca.presentation.reading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.ui.components.BookCard
import com.example.biblioteca.ui.components.CustomButton
import com.example.biblioteca.ui.components.CustomInput
import com.example.biblioteca.ui.theme.BibliotecaTheme

@Composable
fun ReadingScreen(
    viewModel: ReadingViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    ReadingScreenContent(
        state = state,
        onUpdateProgress = viewModel::updateProgress,
        onUpdateStatus = viewModel::updateStatus,
        onBookClick = onBookClick
    )
}

@Composable
fun ReadingScreenContent(
    state: ReadingUiState,
    onUpdateProgress: (bookId: String, currentPage: Int) -> Unit = { _, _ -> },
    onUpdateStatus: (bookId: String, status: ReadingStatus) -> Unit = { _, _ -> },
    onBookClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Minhas leituras",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Acompanhe seu progresso e conclua seus livros.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

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

            state.books.isEmpty() -> {
                Text(
                    text = "Você ainda não tem livros em leitura.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = state.books,
                        key = { it.book.id }
                    ) { libraryBook ->
                        var pageText by remember(libraryBook.currentPage) {
                            mutableStateOf(libraryBook.currentPage.toString())
                        }

                        Column {
                            BookCard(
                                libraryBook = libraryBook,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            CustomInput(
                                value = pageText,
                                onValueChange = { input ->
                                    if (input.all(Char::isDigit)) {
                                        pageText = input
                                    }
                                },
                                label = "Página atual",
                                placeholder = "Digite a pagina atual...",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            val totalPages = libraryBook.book.numberOfPages

                            if (totalPages != null && totalPages > 0) {
                                Text(
                                    text = "Total: $totalPages páginas",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CustomButton(
                                    onClick = {
                                        pageText.toIntOrNull()?.let { page ->
                                            onUpdateProgress(
                                                libraryBook.book.id,
                                                page
                                            )
                                        }
                                    },
                                    text = "Salvar progresso",
                                    modifier = Modifier.weight(1f)
                                )

                                TextButton(
                                    onClick = {
                                        onUpdateStatus(
                                            libraryBook.book.id,
                                            ReadingStatus.COMPLETED
                                        )
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Concluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleReadingBooks = listOf(
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
        status = ReadingStatus.READING,
        currentPage = 120
    )
)

@Preview(showBackground = true)
@Composable
fun ReadingScreenPreview() {
    BibliotecaTheme {
        ReadingScreenContent(
            state = ReadingUiState(
                books = sampleReadingBooks,
                isLoading = false
            )
        )
    }
}