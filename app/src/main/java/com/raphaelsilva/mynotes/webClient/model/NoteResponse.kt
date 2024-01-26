package com.raphaelsilva.mynotes.webClient.model

import com.raphaelsilva.mynotes.model.Note
import java.util.UUID

class NoteResponse(
    val id: String?,
    val title: String?,
    val description: String?,
    val image: String?,
    val color: Int?
) {
    val note: Note
        get() = Note(
            id = id ?: UUID.randomUUID().toString(),
            title = title ?: "",
            description = description ?: "",
            image = image ?: "",
            color = color ?: null
        )
}