package com.example.biblioteca.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "authors"
)
data class AuthorEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val birthDate: String?,

    val deathDate: String?,

    val bio: String?,

    val photoId: Int?
)