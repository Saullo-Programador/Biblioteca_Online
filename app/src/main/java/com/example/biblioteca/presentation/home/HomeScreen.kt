package com.example.biblioteca.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus
import com.example.biblioteca.ui.components.BookCard
import com.example.biblioteca.ui.components.CustomButton
import com.example.biblioteca.ui.theme.BibliotecaTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit = {},
    onOpenLibrary: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    HomeScreenContent(
        state = state,
        onBookClick = onBookClick,
        onOpenLibrary = onOpenLibrary
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onBookClick: (String) -> Unit = {},
    onOpenLibrary: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Minha Biblioteca",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Organize suas leituras e acompanhe seu progresso.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

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

            state.error != null && state.books.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error ?: "Não foi possível carregar os livros."
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Visão geral",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = "Total de livros",
                                value = state.totalBooks,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Em leitura",
                                value = state.bookReading,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = "Concluídos",
                                value = state.bookCompleted,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Quero ler",
                                value = state.booksToRead,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Adicionados recentemente",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (state.recentlySavedBook.isEmpty()) {
                        item {
                            Text(
                                text = "Sua biblioteca ainda está vazia. Pesquise livros e adicione os que deseja ler.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    } else {
                        items(
                            items = state.recentlySavedBook,
                            key = { it.book.id }
                        ) { libraryBook ->
                            BookCard(
                                libraryBook = libraryBook,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        CustomButton(
                            onClick = onOpenLibrary,
                            text = "Ver minha biblioteca"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private val sampleBooks = listOf(
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
    ),
    LibraryBook(
        book = Book(
            id = "3",
            title = "Dom Casmurro",
            authors = listOf("Machado de Assis"),
            coverId = null,
            firstPublishYear = 1899,
            isbn = null,
            publisher = "Livraria Garnier",
            numberOfPages = 256,
            subjects = listOf("Classics")
        ),
        status = ReadingStatus.WANT_TO_READ
    )
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BibliotecaTheme(
        dynamicColor = false
    ) {
        HomeScreenContent(
            state = HomeUiState(
                books = sampleBooks
            )
        )
    }
}