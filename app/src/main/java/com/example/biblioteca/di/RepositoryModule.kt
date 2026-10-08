package com.example.biblioteca.di

import com.example.biblioteca.data.repository.BookRepositoryImpl
import com.example.biblioteca.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        repository: BookRepositoryImpl
    ): BookRepository
}