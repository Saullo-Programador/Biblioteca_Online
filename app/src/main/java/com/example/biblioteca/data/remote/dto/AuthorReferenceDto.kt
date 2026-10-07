package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorReferenceDto(
    @SerialName("author")
    val author: AuthorKeyDto? = null
)