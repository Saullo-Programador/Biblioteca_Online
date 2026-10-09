package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.domain.model.LibraryBook

fun BookEntity.toLibraryBook(): LibraryBook {
    return LibraryBook(
        book = toDomain(),
        status = status,
        currentPage = currentPage,
        rating = rating,
        notes = notes,
        startedAt = startedAt,
        finishedAt = finishedAt,
        savedAt = savedAt
    )
}

fun LibraryBook.toEntity(): BookEntity {
    return BookEntity(
        id = book.id,
        title = book.title,
        authors = book.authors,
        coverId = book.coverId,
        firstPublishYear = book.firstPublishYear,
        isbn = book.isbn,
        publisher = book.publisher,
        numberOfPages = book.numberOfPages,
        subjects = book.subjects,
        status = status,
        currentPage = currentPage,
        rating = rating,
        notes = notes,
        startedAt = startedAt,
        finishedAt = finishedAt,
        savedAt = savedAt
    )
}