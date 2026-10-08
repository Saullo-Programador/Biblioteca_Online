package com.example.biblioteca.di

import android.content.Context
import androidx.room.Room
import com.example.biblioteca.data.local.dao.AuthorDao
import com.example.biblioteca.data.local.dao.BookDao
import com.example.biblioteca.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bookshelf_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookDao(
        database: AppDatabase
    ): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideAuthorDao(
        database: AppDatabase
    ): AuthorDao {
        return database.authorDao()
    }
}