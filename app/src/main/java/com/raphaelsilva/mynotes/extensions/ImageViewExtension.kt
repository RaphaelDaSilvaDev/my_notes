package com.raphaelsilva.mynotes.extensions

import android.widget.ImageView
import coil.load
import com.raphaelsilva.mynotes.R

fun ImageView.loadImage(url: String? = null){
    load(url){
        fallback(R.drawable.imagem_padrao)
        placeholder(R.drawable.imagem_padrao)
    }
}