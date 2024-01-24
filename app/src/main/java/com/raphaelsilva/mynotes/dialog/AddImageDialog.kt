package com.raphaelsilva.mynotes.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.raphaelsilva.mynotes.databinding.DialogAddImageBinding
import com.raphaelsilva.mynotes.extensions.loadImage

class AddImageDialog(private val context: Context) {
    fun show(imageUrl: String? = null, whenLoadImage: (image: String) -> Unit){
        DialogAddImageBinding.inflate(LayoutInflater.from(context)).apply {
            imageUrl.let {
                addImageUrlInput.editText?.setText(it)
                addImageImage.loadImage(it)
            }

            addImageRefresh.setOnClickListener {
                addImageImage.loadImage(addImageUrlInput.editText?.text.toString())
            }

            AlertDialog.Builder(context).setView(root)
                .setNegativeButton("Cancelar"){_,_ ->}
                .setPositiveButton("Aplicar"){_,_ ->
                    val url = addImageUrlInput.editText?.text.toString()
                    whenLoadImage(url)
                }.show()
        }
    }
}