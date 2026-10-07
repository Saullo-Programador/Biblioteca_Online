package com.example.biblioteca.domain.model


data class Author(
    val id: String,
    val name: String,
    val birthDate: String?,
    val deathDate: String?,
    val bio: String?,
    val photoId: Int?
)