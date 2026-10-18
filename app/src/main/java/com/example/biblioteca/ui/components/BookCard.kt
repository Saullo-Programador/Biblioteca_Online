package com.example.biblioteca.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.biblioteca.domain.model.LibraryBook
import com.example.biblioteca.domain.model.ReadingStatus

@Composable
fun BookCard (
    libraryBook: LibraryBook,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
){
    val book = libraryBook.book

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverId?.let {
                    "https://covers.openlibrary.org/b/id/$it-M.jpg"
                },
                contentDescription = "Capa do livro ${book.title}",
                modifier = Modifier.size(
                    width = 88.dp,
                    height = 124.dp
                ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = book.authors.joinToString(", ")
                        .ifBlank { "Autor desconhecido" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (libraryBook.status) {
                        ReadingStatus.WANT_TO_READ ->
                            "Quero ler"
                        ReadingStatus.READING ->
                            "Lendo"
                        ReadingStatus.COMPLETED ->
                            "Concluído"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (libraryBook.status ==
                    ReadingStatus.READING
                ) {
                    val totalPages = book.numberOfPages

                    if (totalPages != null && totalPages > 0) {
                        Text(
                            text = "${libraryBook.currentPage} de $totalPages páginas",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                action?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    it()
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}