package com.example.biblioteca.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.biblioteca.domain.model.ReadingStatus

@Entity(
    tableName = "books"
)
data class BookEntity(

    @PrimaryKey
    val id: String,

    val title: String,

    val authors: List<String>,

    val coverId: Int?,

    val firstPublishYear: Int?,

    val isbn: String?,

    val publisher: String?,

    val numberOfPages: Int?,

    val subjects: List<String>,

    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,

    val currentPage: Int = 0,

    val rating: Float? = null,

    val notes: String? = null,

    val startedAt: Long? = null,

    val finishedAt: Long? = null,

    val savedAt: Long = System.currentTimeMillis()
)