package com.raphaelsilva.mynotes.webClient.model

import com.raphaelsilva.mynotes.model.Note

class NoteRequest(
    val id: String?,
    val title: String?,
    val description: String?,
    val image: String?,
    val color: Int?
) {
    val note: Note
        get() = Note(
            id = 0,
            title = title ?: "",
            description = description ?: "",
            image = image ?: "",
            color = color ?: null
        )
}