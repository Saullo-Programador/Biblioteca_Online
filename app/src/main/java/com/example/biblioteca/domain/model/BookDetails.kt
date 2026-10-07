package com.example.biblioteca.domain.model

data class BookDetails(
    val id: String,
    val title: String,
    val description: String?,
    val firstPublishDate: String?,
    val coverId: Int?,
    val subjects: List<String>,
    val authorIds: List<String>
)