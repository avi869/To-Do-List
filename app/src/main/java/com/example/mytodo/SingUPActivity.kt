package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.mytodo.databinding.ActivitySingUpactivityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sing_in.*
import kotlinx.android.synthetic.main.activity_sing_upactivity.*

class SingUPActivity : AppCompatActivity() {

    //   FOR THE PASSWORD  AUTOMATICALLY ENCRIPTION  &  FOR THE PASSWORD MATCHING

    private lateinit var Email: TextInputEditText
    private lateinit var InputLayoutPassword1: TextInputLayout
    private lateinit var Password: TextInputEditText
    private lateinit var Retypepassword: TextInputEditText


    // GPT  program

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_upactivity)

        InputLayoutPassword1 = findViewById(R.id.InputLayoutPassword1)
        val passwordEditText: TextView = findViewById(R.id.Password)
        val nextEditText: TextView = findViewById(R.id.Retypepassword)

        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = false
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = true
            }
        }

        nextEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = false
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = true
            }
        }


        fun onPasswordFocusChange(view: View, hasFocus: Boolean) {
            if (!hasFocus) {
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = false
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = true
            }
        }

        fun onNextFieldFocusChange(view: View, hasFocus: Boolean) {
            if (hasFocus) {
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = false
                InputLayoutPassword1.isPasswordVisibilityToggleEnabled = true
            }
        }

        val textView: TextView = findViewById(R.id.txt1)
        txt1.setOnClickListener {

            val intent = Intent(this, SingInActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val emailField = findViewById<EditText>(R.id.Email)
        val passwordField = findViewById<EditText>(R.id.Password)
        val retypePasswordField = findViewById<EditText>(R.id.Retypepassword)
        val signUpButton = findViewById<ImageView>(R.id.button)

        signUpButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val retypePassword = retypePasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != retypePassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUp(email, password)
        }
    }

    private fun signUp(email: String, password: String, ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = userData(email, password)
                    database.child("users").child(userId).setValue(user)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT)
                                    .show()
                                // Navigate to sign-in or main activity
                            } else {
                                Toast.makeText(
                                    this,
                                    "Database Error: ${dbTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    val imageView: ImageView = findViewById(R.id.button)
                    button.setOnClickListener {

                        val intent = Intent(this, SingInActivity::class.java)
                        startActivity(intent)
                    }
                }else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

        // firebase program beging

        override fun onBackPressed() {
            super.onBackPressed()

            // val intent = Intent(this, MainActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivity(intent)

            finishAffinity()
        }
    }



