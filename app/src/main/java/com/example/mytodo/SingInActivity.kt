package com.example.mytodo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sing_in.*
import kotlinx.android.synthetic.main.activity_sing_upactivity.*

class SingInActivity : AppCompatActivity() {

    private lateinit var InputLayoutPassword3: TextInputLayout
    private lateinit var Password2: TextInputEditText
    private lateinit var Email2: TextInputEditText

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)

        InputLayoutPassword3 = findViewById(R.id.InputLayoutPassword3)
        Password2 = findViewById(R.id.Password2)
        Email2 = findViewById(R.id.Email2)

        // Add an OnFocusChangeListener to the password EditText
        Password2.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Hide the password when the EditText loses focus
                Password2.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                Password2.setSelection(Password2.text?.length ?: 0)
            }
        }

        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.Email2)
        val passwordField = findViewById<EditText>(R.id.Password2)
        val signInButton = findViewById<ImageView>(R.id.button2)

        val textView: TextView = findViewById(R.id.textView)
        txt2.setOnClickListener {

            val intent = Intent(this, SingUPActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(email, password)
        }
    }
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign-In Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to main Home activity

                    val imageView: ImageView = findViewById(R.id.button2)
                    button2.setOnClickListener {

                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}