package com.example.biblioteca.data.mapper

import com.example.biblioteca.data.local.entity.AuthorEntity
import com.example.biblioteca.data.remote.dto.AuthorDto
import com.example.biblioteca.domain.model.Author

fun AuthorDto.toDomain(): Author {
    return Author(
        id = key
            ?.removePrefix("/authors/")
            ?:"",
        name = name.orEmpty(),
        birthDate = birthDate,
        deathDate = deathDate,
        bio = bio,
        photoId = photos?.firstOrNull()
    )
}


fun AuthorDto.toEntity(): AuthorEntity {
    return AuthorEntity(
        id = key
            ?.removePrefix("/authors/")
            ?:"",
        name = name.orEmpty(),
        birthDate = birthDate,
        deathDate = deathDate,
        bio = bio,
        photoId = photos?.firstOrNull()
    )
}

fun AuthorEntity.toDomain(): Author {
    return Author(
        id = id,
        name = name,
        birthDate = birthDate,
        deathDate = deathDate,
        bio = bio,
        photoId = photoId
    )
}