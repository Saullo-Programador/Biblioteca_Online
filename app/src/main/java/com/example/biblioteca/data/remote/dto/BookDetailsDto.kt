package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailsDto (
    @SerialName("key")
    val key: String? = null,

    @SerialName("title")
    val title: String? = null,

    @Serializable(with = DescriptionSerializer::class)
    @SerialName("description")
    val description: String? = null,

    @SerialName("first_publish_date")
    val firstPublishDate: String? = null,

    @SerialName("covers")
    val covers: List<Int>? = null,

    @SerialName("subjects")
    val subjects: List<String>? = null,

    @SerialName("authors")
    val authors: List<AuthorReferenceDto>? = null
)