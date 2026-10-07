package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponseDto (
    @SerialName("numFound")
    val numFound: Int = 0,

    @SerialName("start")
    val start: Int = 0,

    @SerialName("docs")
    val docs: List<BookDto> = emptyList()

)