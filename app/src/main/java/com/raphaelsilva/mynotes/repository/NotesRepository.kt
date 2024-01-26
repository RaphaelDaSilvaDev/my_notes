package com.raphaelsilva.mynotes.repository

import com.raphaelsilva.mynotes.database.dao.NoteDao
import com.raphaelsilva.mynotes.model.Note
import com.raphaelsilva.mynotes.webClient.NotesWebClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotesRepository(private val noteDao: NoteDao, private val webClient: NotesWebClient) {
    fun getAllEnabled(): Flow<List<Note>>{
        return noteDao.getAllEnabled()
    }

    private suspend fun update(){
        webClient.getAll()?.let { notes ->
            val syncNotes = notes.map { note ->
                note.copy(synchronize = true)
            }
            noteDao.save(syncNotes)
        }
    }

    fun getById(id: String): Flow<Note> {
        return noteDao.getById(id)
    }

    suspend fun save(note: Note) {
        noteDao.save(note)
        saveWebClient(note)
    }

    private suspend fun saveWebClient(note: Note) {
        if (webClient.save(note)) {
            val syncNote = note.copy(synchronize = true)
            noteDao.save(syncNote)
        }
    }

    suspend fun delete(id: String) {
        noteDao.disable(id)
        deleteWebClient(id)
    }

    private suspend fun deleteWebClient(id: String) {
        if (webClient.delete(id)) {
            noteDao.delete(id)
        }
    }

    suspend fun synchronize(){
        val nonSyncNotes = noteDao.getNonSyncNotes().first()
        nonSyncNotes.forEach { note ->
            saveWebClient(note)
        }
        syncDelete()
        update()
    }

    private suspend fun syncDelete(){
        val deleteNote = noteDao.getAllDisabled().first()
        deleteNote.forEach { note ->
            deleteWebClient(note.id)
        }
    }
}