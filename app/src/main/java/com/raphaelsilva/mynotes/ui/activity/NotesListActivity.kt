package com.raphaelsilva.mynotes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.raphaelsilva.mynotes.R
import com.raphaelsilva.mynotes.database.AppDatabase
import com.raphaelsilva.mynotes.databinding.ActivityNotesListBinding
import com.raphaelsilva.mynotes.repository.NotesRepository
import com.raphaelsilva.mynotes.ui.recyclerview.adapter.NotesListAdapter
import com.raphaelsilva.mynotes.webClient.NotesWebClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesListActivity : AppCompatActivity(R.layout.activity_notes_list) {
    private val binding by lazy {
        ActivityNotesListBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        NotesListAdapter(this)
    }

    private val notesRepository by lazy {
        NotesRepository(
            AppDatabase.instance(this).noteDao(), NotesWebClient()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settingRefresh()
        settingRecyclerView()
        lifecycleScope.launch {
            launch(Dispatchers.IO) {
                notesRepository.synchronize()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateAdapter()
            }

        }

        binding.notesListCreateNewNote.setOnClickListener {
            val intent = Intent(this, NotesFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun settingRefresh() {
        binding.noteListRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO) {
                notesRepository.synchronize()
                binding.noteListRefresh.isRefreshing = false
            }
        }
    }

    private suspend fun updateAdapter() {
        notesRepository.getAllEnabled().collect { notes ->
            binding.notesListEmptyText.visibility = if (notes.isEmpty()) {
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