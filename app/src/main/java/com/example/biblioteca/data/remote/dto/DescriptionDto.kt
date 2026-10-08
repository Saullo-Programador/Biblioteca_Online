package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DescriptionDto(
    val type: String? = null,
    val value: String? = null
)