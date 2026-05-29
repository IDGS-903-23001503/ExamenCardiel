package com.example.examencardiel

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader

class DesencriptarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desencriptar)

        val mainLayout = findViewById<LinearLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.spinnerMensajes)
        val etKey = findViewById<EditText>(R.id.etDesencriptarKey)
        val btnDesencriptar = findViewById<Button>(R.id.btnProcesarDesencriptar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val listaRegistros = leerArchivo()
        val mensajesEncriptados = ArrayList<String>()

        for (linea in listaRegistros) {
            val partes = linea.split(",")
            mensajesEncriptados.add(partes[2])
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mensajesEncriptados)
        spinner.adapter = adapter

        btnDesencriptar.setOnClickListener {
            if (listaRegistros.isEmpty()) return@setOnClickListener

            val index = spinner.selectedItemPosition
            val datos = listaRegistros[index].split(",")
            val original = datos[0]
            val encriptado = datos[2]
            val clave = etKey.text.toString().toIntOrNull() ?: 0

            val resultado = cifrarCesar(encriptado, clave, false)

            if (resultado == original) {
                tvResultado.text = "Texto original: $encriptado, el resultado: $resultado\n"
            } else {
                tvResultado.text = "Error, intenta otro número."
            }
        }
    }

    private fun leerArchivo(): ArrayList<String> {
        val lista = ArrayList<String>()
        try {
            val reader = BufferedReader(InputStreamReader(openFileInput("datos.txt")))
            var linea = reader.readLine()
            while (linea != null) {
                lista.add(linea)
                linea = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) { e.printStackTrace() }
        return lista
    }

    private fun cifrarCesar(texto: String, shift: Int, esEncriptar: Boolean): String {
        var resultado = ""
        val factor = if (esEncriptar) shift else -shift

        for (i in 0 until texto.length) {
            val char = texto[i]
            if (char in 'A'..'Z') {
                resultado += ('A'.toInt() + (char - 'A' + factor % 26 + 26) % 26).toChar()
            } else if (char in 'a'..'z') {
                resultado += ('a'.toInt() + (char - 'a' + factor % 26 + 26) % 26).toChar()
            } else {
                resultado += char
            }
        }
        return resultado
    }
}