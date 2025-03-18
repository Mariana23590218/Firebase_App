package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PagPrincipal : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var emailT: TextView
    private lateinit var nombre: TextView
    private lateinit var cerrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pag_principal)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        emailT = findViewById(R.id.emailT)
        nombre = findViewById(R.id.nombre)
        cerrar = findViewById(R.id.cerrar)

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // Display email
            emailT.text = "Email: ${currentUser.email}"

            // Get user data from Firestore
            firestore.collection("usuarios")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        var nombreT = document.getString("nombre")
                        nombreT = "Nombre: $nombre"
                    }
                }
        } else {
            // No user is signed in, return to login
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Set logout button click listener
        cerrar.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}