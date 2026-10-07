package com.example.biblioteca.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.biblioteca.data.local.entity.AuthorEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface AuthorDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthors(author: List<AuthorEntity>)

    @Update
    suspend fun updateAuthor(author: AuthorEntity)

    @DELETE
    suspend fun deleteAuthor(author: AuthorEntity)

    @Query("SELECT * FROM authors WHERE id = :authorId")
    suspend fun getAuthorById(
        authorId: String
    ): AuthorEntity?

    @Query("SELECT * FROM authors WHERE id = :authorId")
    suspend fun observeAuthorById(
        authorId: String
    ): List<AuthorEntity?>

    @Query("SELECT * FROM authors ORDER BY name ASC")
    suspend fun observeAllAuthors(): Flow<List<AuthorEntity>>

    @Query(
        """
            SELECT * FROM authors
            WHERE name LIKE '%' || :query || '%'
            ORDER BY name ASC
        """
    )
    fun searchAuthors(
        query: String
    ): Flow<List<AuthorEntity>>

    @Query("DELETE FROM authors")
    suspend fun deleteAllAuthors()

}