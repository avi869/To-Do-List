package com.example.mytodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytodo.databinding.ActivityUpdateBinding
import com.google.firebase.database.*
import com.google.firebase.database.core.view.View
import kotlinx.android.synthetic.main.activity_crud.*

class updateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteId = intent.getStringExtra("NOTE_ID")
        noteId?.let { getNoteById(it) }

        binding.updatebutton.setOnClickListener {
            noteId?.let { updateNote(it) }
        }
    }

    private fun getNoteById(noteId: String) {
        val database = FirebaseDatabase.getInstance()
        val noteRef = database.getReference("notes").child(noteId)

        noteRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val note = snapshot.getValue(Note::class.java)
                note?.let {
                    binding.updatetitle.setText(it.title)
                    binding.updatecontent.setText(it.content)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@updateActivity, "Failed to load note.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateNote(noteId: String) {
        val title = binding.updatetitle.text.toString().trim()
        val content = binding.updatecontent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            // Show error message
            Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val noteRef = database.getReference("notes").child(noteId)

        val updatedNote = Note(
            id = noteId,
            title = title,
            content = content
        )

        noteRef.setValue(updatedNote).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                // Note updated successfully
                Toast.makeText(this, "Note updated successfully.", Toast.LENGTH_SHORT).show()
                finish()
                // Navigate back or show success message
            } else {
                // Handle error
                Toast.makeText(this, "Failed to update note.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}