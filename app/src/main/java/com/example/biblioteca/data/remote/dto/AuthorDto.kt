package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto (
    @SerialName("key")
    val key: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("birth_date")
    val birthDate: String? = null,

    @SerialName("death_date")
    val deathDate: String? = null,

    @SerialName("bio")
    val bio: String? = null,

    @SerialName("photos")
    val photos: List<Int>? = null

)