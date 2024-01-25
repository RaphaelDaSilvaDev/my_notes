package com.raphaelsilva.mynotes.webClient

import com.raphaelsilva.mynotes.webClient.services.NoteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInit {
    private val client by lazy {
        val logg = HttpLoggingInterceptor()

        logg.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(logg).build()
    }

    private val retrofit = Retrofit.Builder().baseUrl("http://172.31.128.1:8080/").client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val noteService = retrofit.create(NoteService::class.java)
}