package com.raphaelsilva.mynotes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.raphaelsilva.mynotes.R
import com.raphaelsilva.mynotes.database.AppDatabase
import com.raphaelsilva.mynotes.databinding.ActivityNotesListBinding
import com.raphaelsilva.mynotes.model.Note
import com.raphaelsilva.mynotes.ui.recyclerview.adapter.NotesListAdapter
import com.raphaelsilva.mynotes.webClient.RetrofitInit
import com.raphaelsilva.mynotes.webClient.model.NoteRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateAdapter()
            }
        }

        binding.notesListCreateNewNote.setOnClickListener {
            val intent = Intent(this, NotesFormActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val call: Call<List<NoteRequest>> = RetrofitInit().noteService.getAll()
            val response = call.execute()
            response.body()?.let { notesRequest ->
                val notes = notesRequest.map {
                    it.note
                }
                Log.i("RetrofitNotes", "onCreate: $notes")
            }

        }
    }

    private suspend fun updateAdapter() {
        noteDao.getAll().collect { notes ->
            binding.notesListEmptyText.visibility =
                if (notes.isEmpty()) {
                    binding.notesListRecycler.visibility = View.GONE
                    View.VISIBLE
                } else {
                    binding.notesListRecycler.visibility = View.VISIBLE
                    adapter.update(notes)
                    View.GONE
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