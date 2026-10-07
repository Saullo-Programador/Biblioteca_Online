package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorKeyDto(
    @SerialName("key")
    val key: String? = null
)