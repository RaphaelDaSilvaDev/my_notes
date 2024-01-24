package com.raphaelsilva.mynotes.exceptions

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler

class CoroutineException {
    companion object{
        fun handler(context: Context): CoroutineExceptionHandler{
            return CoroutineExceptionHandler{coroutineContext, throwable ->
                Log.e("CoroutineException", "handler: Ocorreu um Problema $throwable", )
                Toast.makeText(context,"Ocorreu um Problema", Toast.LENGTH_LONG).show()
            }
        }
    }
}