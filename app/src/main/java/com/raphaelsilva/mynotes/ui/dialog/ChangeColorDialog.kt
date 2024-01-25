package com.raphaelsilva.mynotes.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.raphaelsilva.mynotes.databinding.DialogChangeColorBinding

class ChangeColorDialog(private val context: Context) {
    fun show(whenChangeColor: (color: String) -> Unit){
        DialogChangeColorBinding.inflate(LayoutInflater.from(context)).apply {
            AlertDialog.Builder(context).setView(root)
                .setNegativeButton("Remover"){_,_ -> whenChangeColor("")}
                .setNeutralButton("Cancelar"){_, _ -> }
                .setPositiveButton("Aplicar"){_,_ ->
                    whenChangeColor("apply")
                }.show()
        }
    }
}