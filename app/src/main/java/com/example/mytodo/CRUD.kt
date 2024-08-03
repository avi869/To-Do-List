package com.example.mytodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mytodo.databinding.ActivityCrudBinding
import com.example.mytodo.databinding.ActivityHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crud.*

class CRUD : AppCompatActivity() {

    private lateinit var binding: ActivityCrudBinding
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.donebutton.setOnClickListener {
            val title = binding.title.text.toString().trim()
            val content = binding.content.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            databaseReference = FirebaseDatabase.getInstance().reference.child("notes")
            val noteId = databaseReference.push().key // Generate a unique ID
            val dataclass = noteId?.let { Note(it, title, content) }
            noteId?.let {

                // now create a step by step path to store data & child should be always Unique
                databaseReference.child(it).setValue(dataclass).addOnSuccessListener {
                    binding.title.text?.clear()
                    binding.content.text?.clear()

                    Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CRUD, Home::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to save note: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

