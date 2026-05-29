package com.example.examencardiel

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EncriptarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_encriptar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etFrase = findViewById<EditText>(R.id.etFrase)
        val etDesplazamiento = findViewById<EditText>(R.id.etDesplazamiento)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val texto = etFrase.text.toString()
            val shift = etDesplazamiento.text.toString().toIntOrNull() ?: 0
            val encriptado = cifrarCesar(texto, shift, true)

            openFileOutput("datos.txt", MODE_APPEND).bufferedWriter().use {
                it.write("$texto,$shift,$encriptado\n")
            }
            Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cifrarCesar(texto: String, shift: Int, encrypt: Boolean): String {
        val factor = if (encrypt) shift else -shift
        return texto.map { char ->
            when (char) {
                in 'A'..'Z' -> ('A'.toInt() + (char.toInt() - 'A'.toInt() + factor % 26 + 26) % 26).toChar()
                in 'a'..'z' -> ('a'.toInt() + (char.toInt() - 'a'.toInt() + factor % 26 + 26) % 26).toChar()
                else -> char
            }
        }.joinToString("")
    }
}