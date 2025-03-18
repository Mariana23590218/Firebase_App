package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore:FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    private lateinit var email: EditText
    private lateinit var contraseña: EditText
    private lateinit var iniciar: Button
    private lateinit var registrar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        email = findViewById(R.id.email)
        contraseña = findViewById(R.id.contraseña)
        iniciar = findViewById(R.id.ingresar)
        registrar = findViewById(R.id.cerrar)

        iniciar.setOnClickListener {
            loginUser()
        }

        registrar.setOnClickListener {
            registrarUser()
        }
    }

    private fun loginUser() {
        val email = email.text.toString().trim()
        val password = contraseña.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val intent = Intent(this, PagPrincipal::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    // Login failed
                    Toast.makeText(this, "Error de autenticación: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registrarUser() {
        val email = email.text.toString().trim()
        val password = contraseña.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                    // Create user document in Firestore
                    val user = hashMapOf(
                        "email" to email,
                        "nombre" to "Usuario"
                    )

                    firestore.collection("usuarios")
                        .document(firebaseAuth.currentUser?.uid ?: "")
                        .set(user)
                } else {
                    // Registration failed
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}