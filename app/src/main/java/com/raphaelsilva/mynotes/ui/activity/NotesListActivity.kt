package com.raphaelsilva.mynotes.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raphaelsilva.mynotes.R
import com.raphaelsilva.mynotes.database.AppDatabase
import com.raphaelsilva.mynotes.databinding.ActivityNotesListBinding
import com.raphaelsilva.mynotes.exceptions.CoroutineException
import com.raphaelsilva.mynotes.model.Note
import com.raphaelsilva.mynotes.ui.recyclerview.adapter.NotesListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotesListActivity : AppCompatActivity(R.layout.activity_notes_list) {
    private val binding by lazy {
        ActivityNotesListBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        NotesListAdapter(this)
    }

    private val noteDao by lazy {
        AppDatabase.instance(this).noteDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settingRecyclerView()
        updateAdapter()

        binding.notesListCreateNewNote.setOnClickListener {
            val intent = Intent(this, NotesFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateAdapter(){
        lifecycleScope.launch(CoroutineException.handler(this)) {
            noteDao.getAll().collect{note ->
                adapter.update(note)
            }
        }
    }

    private fun settingRecyclerView() {
        binding.notesListRecycler.adapter = adapter
        adapter.onClickNote = { note ->
            val intent = Intent(this, NotesFormActivity::class.java).apply {
                putExtra(NOTE_ID, note.id)
            }
            startActivity(intent)
        }
    }
}