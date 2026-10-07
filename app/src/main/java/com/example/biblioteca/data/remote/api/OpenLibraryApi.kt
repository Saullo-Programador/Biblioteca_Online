package com.example.biblioteca.data.remote.api

import retrofit2.http.GET

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks():Book
}