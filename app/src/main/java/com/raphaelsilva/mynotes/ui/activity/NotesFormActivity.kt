package com.raphaelsilva.mynotes.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.RadioButton
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raphaelsilva.mynotes.R
import com.raphaelsilva.mynotes.database.AppDatabase
import com.raphaelsilva.mynotes.databinding.ActivityNotesFormBinding
import com.raphaelsilva.mynotes.ui.dialog.AddImageDialog
import com.raphaelsilva.mynotes.ui.dialog.ChangeColorDialog
import com.raphaelsilva.mynotes.exceptions.CoroutineException
import com.raphaelsilva.mynotes.extensions.loadImage
import com.raphaelsilva.mynotes.model.Note
import kotlinx.coroutines.launch

class NotesFormActivity : AppCompatActivity(R.layout.activity_notes_form) {
    private val binding by lazy {
        ActivityNotesFormBinding.inflate(layoutInflater)
    }

    private val noteDao by lazy {
        AppDatabase.instance(this).noteDao()
    }

    private var noteId: Long = 0L
    private var imageUrl: String? = null
    private var selectedColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        clickMoreButton()
        onDeleteButton()
        onSaveButton()
        loadNote()

        val visible = if (imageUrl != null) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.notesFormImage.visibility = visible
    }

    private fun loadNote() {
        noteId = intent.getLongExtra(NOTE_ID, 0)
        lifecycleScope.launch(CoroutineException.handler(this)) {
            noteDao.getById(noteId).collect { note ->
                if (note != null) {
                    bindingNote(note)
                }
            }
        }
    }

    private fun bindingNote(note: Note) {
        binding.notesFormTitle.setText(note.title)
        binding.notesFormDesc.setText(note.description)

        note?.image.let { image ->
            if (!image.isNullOrEmpty()) {
                binding.notesFormImage.loadImage(image)
                imageUrl = image
                binding.notesFormImage.visibility = View.VISIBLE
            }
        }

        selectedColor = note.color
        if (selectedColor != null) {
            val background = binding.notesFormBody
            binding.notesFormBarColor.setBackgroundColor(resources.getColor(selectedColor!!))
            background.setBackgroundColor(resources.getColor(selectedColor!!))
            background.background.alpha = 50
        }
    }

    private fun clickMoreButton() {
        binding.notesFormMore.setOnClickListener {
            showMenu(it, R.menu.note_form_menu)
        }
    }

    private fun bindingImage() {
        val imageInput = binding.notesFormImage
        AddImageDialog(this).show(imageUrl) { image ->
            imageInput.loadImage(image)
            binding.notesFormImage.visibility = View.VISIBLE
            imageUrl = image
        }
    }

    private fun bindingColor() {
        ChangeColorDialog(this).show {
            val background = binding.notesFormBody
            if (it.isNullOrBlank()) {
                binding.notesFormBarColor.setBackgroundColor(resources.getColor(android.R.color.transparent))
                background.setBackgroundColor(resources.getColor(android.R.color.transparent))
                selectedColor = null
            } else if (it === "apply" && !selectedColor.toString().isNullOrBlank()) {
                binding.notesFormBarColor.setBackgroundColor(resources.getColor(selectedColor!!))
                background.setBackgroundColor(resources.getColor(selectedColor!!))
                background.background.alpha = 50
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)

        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.note_form_menu_add_image -> {
                    bindingImage()
                    true
                }

                R.id.note_form_menu_change_color -> {
                    bindingColor()
                    true
                }

                else -> {
                    true
                }
            }
        }

        popup.show()
    }

    private fun onSaveButton() {
        val formSaveButton = binding.notesFormSave
        formSaveButton.setOnClickListener {
            lifecycleScope.launch(CoroutineException.handler(this)) {
                val newNote = note()
                noteDao.save(newNote)
                finish()
            }
        }
    }

    private fun onDeleteButton() {
        binding.notesFormDelete.setOnClickListener {
            lifecycleScope.launch(CoroutineException.handler(this)) {
                noteDao.delete(noteId)
                finish()
            }
        }
    }

    private fun note(): Note {
        return Note(
            id = noteId,
            title = binding.notesFormTitle.text.toString(),
            description = binding.notesFormDesc.text.toString(),
            image = imageUrl,
            color = selectedColor
        )
    }

    fun onColorChange(view: View) {
        if (view is RadioButton) {
            when (view.id) {
                R.id.change_color_red -> {
                    selectedColor = R.color.red
                }

                R.id.change_color_pink -> {
                    selectedColor = R.color.pink
                }

                R.id.change_color_purple -> {
                    selectedColor = R.color.purple
                }

                R.id.change_color_blue -> {
                    selectedColor = R.color.blue
                }

                R.id.change_color_cyan -> {
                    selectedColor = R.color.cyan
                }

                R.id.change_color_green -> {
                    selectedColor = R.color.green
                }

                R.id.change_color_yellow -> {
                    selectedColor = R.color.yellow
                }

                R.id.change_color_orange -> {
                    selectedColor = R.color.orange
                }

                R.id.change_color_brown -> {
                    selectedColor = R.color.brown
                }

                R.id.change_color_teal -> {
                    selectedColor = R.color.deep_orange
                }
            }
        }
    }
}