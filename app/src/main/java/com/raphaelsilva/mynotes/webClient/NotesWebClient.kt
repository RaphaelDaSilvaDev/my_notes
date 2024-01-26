package com.raphaelsilva.mynotes.webClient

import android.util.Log
import com.raphaelsilva.mynotes.model.Note
import com.raphaelsilva.mynotes.webClient.model.NoteRequest

private const val TAG = "NoteWebClient"
class NotesWebClient {

    private val noteService = RetrofitInit().noteService
    suspend fun getAll(): List<Note>? {
         return try {
            noteService.getAll().map { noteResponse ->
                noteResponse.note
            }
        } catch (e: Exception){
            Log.e(TAG, "getAll: Error", e)
            return null
        }
    }

    suspend fun save(note: Note): Boolean{
        try {
            val response = noteService.save(note.id, NoteRequest(
                title = note.title,
                description = note.description,
                image = note.image,
                color = note.color
            ))
            
            return response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "save: Error", e)
        }
        return false
    }

    suspend fun delete(id: String): Boolean {
        try {
            noteService.delete(id)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "delete: Error", e)
        }
        return false
    }
}