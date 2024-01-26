package com.raphaelsilva.mynotes.webClient.services

import androidx.room.Delete
import com.raphaelsilva.mynotes.webClient.model.NoteRequest
import com.raphaelsilva.mynotes.webClient.model.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteService {
    @GET("notas")
    suspend fun getAll(): List<NoteResponse>

    @PUT("notas/{id}")
    suspend fun save(@Path("id") id: String, @Body note: NoteRequest): Response<NoteResponse>

    @DELETE("notas/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}