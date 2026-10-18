package com.example.biblioteca.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biblioteca.ui.theme.BibliotecaTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar por título ou autor...",
    onSearch: (String) -> Unit = {},
    onClearQuery: () -> Unit = { onQueryChange("") },
    enabled: Boolean = true
) {
    CustomInput(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        enabled = enabled,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ícone de busca"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpar busca"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    BibliotecaTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SearchBar(
                query = "Dom Casmurro",
                onQueryChange = {}
            )
        }
    }
}