package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.dao.BookDao
import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.data.remote.dto.BookDetailsDto
import com.example.biblioteca.data.remote.dto.BookDto
import com.example.biblioteca.domain.model.Book
import com.example.biblioteca.domain.model.BookDetails
import com.example.biblioteca.domain.model.ReadingStatus

fun BookDto.toDomain(): Book {
    return Book(
        id = key
            ?.removePrefix("/works/")
            ?: "",
        title = title.orEmpty(),
        authors = authors.orEmpty(),
        coverId = coverId,
        firstPublishYear = firstPublishYear,
        isbn = isbns?.firstOrNull(),
        publisher = publishers?.firstOrNull(),
        numberOfPages = numberOfPages,
        subjects = subjects.orEmpty()
    )
}

fun BookDto.toEntity(
    status: ReadingStatus = ReadingStatus.WANT_TO_READ
): BookEntity {
    return BookEntity(
        id = key
            ?.removePrefix("/works/")
            ?: "",
        title = title.orEmpty(),
        authors = authors.orEmpty(),
        coverId = coverId,
        firstPublishYear = firstPublishYear,
        isbn = isbns?.firstOrNull(),
        publisher = publishers?.firstOrNull(),
        numberOfPages = numberOfPages,
        subjects = subjects.orEmpty(),
        status = status
    )
}

fun BookEntity.toDomain(): Book{
    return Book(
        id = id,
        title = title,
        authors = authors,
        coverId = coverId,
        firstPublishYear = firstPublishYear,
        isbn = isbn,
        publisher = publisher,
        numberOfPages = numberOfPages,
        subjects = subjects,
    )
}

fun BookDetailsDto.toDomain(): BookDetails{
    return BookDetails(
        id = key
            ?.removePrefix("/works/")
            ?: "",
        title = title.orEmpty(),
        description = extractDescription(),
        firstPublishDate = firstPublishDate,
        coverId = covers?.firstOrNull(),
        subjects = subjects.orEmpty(),
        authorIds = authors
            .orEmpty()
            .mapNotNull { reference ->
                reference.author?.key
                    ?.removePrefix("/works/")
            }
    )
}

private fun BookDetailsDto.extractDescription(): String? {
    return when (description) {
        null -> null
        else -> description
    }
}