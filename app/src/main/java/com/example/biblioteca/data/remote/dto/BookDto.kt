package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto (
    @SerialName("key")
    val key: String? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("author_name")
    val authors: List<String>? = null,

    @SerialName("author_key")
    val authorKeys: List<String>? = null,

    @SerialName("first_publish_year")
    val firstPublishYear: Int? = null,

    @SerialName("isbn")
    val isbns: List<String>? = null,

    @SerialName("cover_i")
    val coverId: Int? = null,

    @SerialName("edition_key")
    val editionKeys: List<String>? = null,

    @SerialName("publisher")
    val publishers: List<String>? = null,

    @SerialName("language")
    val languages: List<String>? = null,

    @SerialName("number_of_pages_median")
    val numberOfPages: Int? = null,

    @SerialName("subject")
    val subjects: List<String>? = null
)