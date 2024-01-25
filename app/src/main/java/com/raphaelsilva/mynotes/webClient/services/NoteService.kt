package com.raphaelsilva.mynotes.webClient.services

import com.raphaelsilva.mynotes.webClient.model.NoteRequest
import retrofit2.Call
import retrofit2.http.GET

interface NoteService {
    @GET("notas")
    fun getAll(): Call<List<NoteRequest>>
}