package com.example.biblioteca.domain.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val coverId: Int?,
    val firstPublishYear: Int?,
    val isbn: String?,
    val publisher: String?,
    val numberOfPages: Int?,
    val subjects: List<String>
)