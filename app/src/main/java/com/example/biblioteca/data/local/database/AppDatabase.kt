package com.example.biblioteca.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.biblioteca.data.local.converter.RoomConverters
import com.example.biblioteca.data.local.dao.AuthorDao
import com.example.biblioteca.data.local.dao.BookDao
import com.example.biblioteca.data.local.entity.AuthorEntity
import com.example.biblioteca.data.local.entity.BookEntity

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun authorDao(): AuthorDao
}