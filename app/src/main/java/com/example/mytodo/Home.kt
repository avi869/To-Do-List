package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodo.databinding.ActivityHomeBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notes: ArrayList<Note>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_home)

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("notes")

        val imageView: ImageView = findViewById(R.id.addtaskbutton)
        addtaskbutton.setOnClickListener {
            val intent = Intent(this, CRUD::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerview)
        notes = ArrayList()
        notesAdapter = NotesAdapter(notes) { noteId ->
            val intent = Intent(this, updateActivity::class.java)
            intent.putExtra("Note_ID", noteId)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        fetchNotes()
    }

    private fun fetchNotes() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear()
                for (dataSnapshot in snapshot.children) {
                    val note = dataSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        note.id = dataSnapshot.key ?: "" // Ensure the id is set
                        notes.add(note)
                       // Log.d("HomeActivity", "Note added: ${note.title}, ${note.content}")
                    }
                }
                notesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Home, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
