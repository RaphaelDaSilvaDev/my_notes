package com.raphaelsilva.mynotes.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raphaelsilva.mynotes.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE disabled = 0")
    fun getAllEnabled(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE disabled = 1")
    fun getAllDisabled(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id AND disabled = 0")
    fun getById(id: String): Flow<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(note: List<Note>)

    @Query("DELETE FROM Note WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM Note WHERE synchronize = 0 AND disabled = 0")
    fun getNonSyncNotes(): Flow<List<Note>>

    @Query("UPDATE Note SET disabled = 1 WHERE id = :id")
    suspend fun disable(id: String)
}