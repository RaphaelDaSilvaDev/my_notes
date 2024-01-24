package com.raphaelsilva.mynotes.ui.recyclerview.adapter

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raphaelsilva.mynotes.R
import com.raphaelsilva.mynotes.databinding.NoteItemBinding
import com.raphaelsilva.mynotes.extensions.loadImage
import com.raphaelsilva.mynotes.model.Note


class NotesListAdapter (private val context: Context, var onClickNote: (note: Note) -> Unit ={}, notes: List<Note> = emptyList()): RecyclerView.Adapter<NotesListAdapter.ViewHolder>(){
    private val notes = notes.toMutableList()

    class ViewHolder(
        private val binding: NoteItemBinding,
        private val onClickNote: (note: Note) -> Unit,
        private val context: Context
    ): RecyclerView.ViewHolder(binding.root){
        private lateinit var note: Note

        init {
            itemView.setOnClickListener {
                if(::note.isInitialized){
                    onClickNote(note)
                }
            }
        }

        fun bindingNote(note: Note){
            this.note = note
            val noteImage = binding.noteItemImage
            noteImage.visibility = if(note.image.isNullOrBlank()){
                View.GONE
            }else{
                noteImage.loadImage(note.image)
                View.VISIBLE
            }

            if(note.color == null){
                when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.noteItemCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray_200))
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.noteItemCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        binding.noteItemCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                }
            }else{
                binding.noteItemCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, note.color!!))
            }
            binding.noteItemTitle.text = note.title
            binding.noteItemDesc.text = note.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(NoteItemBinding.inflate(
        LayoutInflater.from(context)), onClickNote, context)

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.bindingNote(note)
    }

    fun update(notesList: List<Note>){
        notes.clear()
        notes.addAll(notesList)
        notifyDataSetChanged()
    }

}