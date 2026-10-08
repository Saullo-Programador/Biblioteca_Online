package com.example.biblioteca.data.remote.api

import com.example.biblioteca.data.remote.dto.AuthorDto
import com.example.biblioteca.data.remote.dto.BookDetailsDto
import com.example.biblioteca.data.remote.dto.BookSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BookSearchResponseDto

    @GET("search.json")
    suspend fun searchBooksByTitle(
        @Query("title") title: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BookSearchResponseDto

    @GET("search.json")
    suspend fun searchBooksByAuthor(
        @Query("author") author: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BookSearchResponseDto

    @GET("search.json")
    suspend fun searchBooksByIsbn(
        @Query("isbn") isbn: String
    ): BookSearchResponseDto

    @GET("works/{workId}.json")
    suspend fun getBookDetails(
        @Path("workId") workId: String
    ): BookDetailsDto

    @GET("author/{authorId}.json")
    suspend fun getAuthor(
        @Path("authorId") authorId: String
    ): AuthorDto

    @GET("author/{authorId}/works.json")
    suspend fun getAuthorWorks(
        @Path("authorId") authorId: String,
        @Query("limit") limit: Int = 20
    ): BookSearchResponseDto
}