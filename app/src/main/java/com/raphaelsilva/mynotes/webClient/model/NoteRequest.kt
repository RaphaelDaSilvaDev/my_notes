package com.raphaelsilva.mynotes.webClient.model

class NoteRequest (
    val title: String,
    val description: String,
    val image: String? = null ,
    val color: Int? = null
)