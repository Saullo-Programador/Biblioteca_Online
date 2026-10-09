package com.example.biblioteca.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.biblioteca.data.local.entity.BookEntity
import com.example.biblioteca.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(book: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun observeBookById(bookId: String): Flow<BookEntity?>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT * FROM books ORDER BY savedAt DESC")
    fun observeAllBooks(): Flow<List<BookEntity>>

    @Query(
        """
            SELECT * FROM books
            WHERE status = :status
            ORDER BY savedAt DESC
        """
    )
    fun observeBooksByStatus(
        status: ReadingStatus
    ): Flow<List<BookEntity>>

    @Query(
        """
            SELECT * FROM books
            WHERE title LIKE '%' || :query || '%'
            OR authors LIKE '%' || :query || '%'
            ORDER BY title ASC
        """
    )
    fun searchSavedBooks(
        query: String
    ): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY rating DESC")
    fun observeBooksByRating(): Flow<List<BookEntity>>

    @Query(
        """
            UPDATE books
            SET currentPage = :currentPage
            WHERE id = :bookId
        """
    )
    suspend fun updateReadingProgress(
        bookId: String,
        currentPage: Int
    )

    @Query(
        """
            UPDATE books
            SET status = :status
            WHERE id = :bookId
        """
    )
    suspend fun updateReadingStatus(
        bookId: String,
        status: ReadingStatus
    )

    @Query(
        """
            UPDATE books
            SET rating = :rating
            WHERE id = :bookId
        """
    )
    suspend fun updateRating(
        bookId: String,
        rating: Float?
    )

    @Query(
        """
            UPDATE books
            SET notes = :notes
            WHERE id = :bookId
        """
    )
    suspend fun updateNotes(
        bookId: String,
        notes: String?
    )

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
}