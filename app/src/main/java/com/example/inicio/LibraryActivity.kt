package com.example.inicio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import Adapters.AuthorAdapter
import Controllers.AuthorController
import Data.ApiDataManager
import Person.Author

class LibraryActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var etAuthorId: TextInputEditText
    private lateinit var etAuthorName: TextInputEditText
    private lateinit var btnAddAuthor: Button
    private lateinit var btnUpdateAuthor: Button
    private lateinit var btnClearAuthor: Button
    private lateinit var rvAuthors: RecyclerView

    private lateinit var authorController: AuthorController
    private lateinit var authorAdapter: AuthorAdapter
    private var editingAuthorId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupToolbar()
        initController()
        setupRecyclerView()
        setupListeners()
        loadAuthors()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        etAuthorId = findViewById(R.id.etAuthorId)
        etAuthorName = findViewById(R.id.etAuthorName)
        btnAddAuthor = findViewById(R.id.btnAddAuthor)
        btnUpdateAuthor = findViewById(R.id.btnUpdateAuthor)
        btnClearAuthor = findViewById(R.id.btnClearAuthor)
        rvAuthors = findViewById(R.id.rvAuthors)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_authors -> {
                true
            }
            R.id.menu_books -> {
                startActivity(Intent(this, LibraryActivity2::class.java))
                true
            }
            R.id.menu_loans -> {
                startActivity(Intent(this, LibraryActivity3::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initController() {
        authorController = AuthorController(ApiDataManager())
    }

    private fun setupRecyclerView() {
        authorAdapter = AuthorAdapter(
            mutableListOf(),
            onEditClick = { author -> loadAuthorForEditing(author) },
            onDeleteClick = { author -> deleteAuthor(author) }
        )
        rvAuthors.layoutManager = LinearLayoutManager(this)
        rvAuthors.adapter = authorAdapter
    }

    private fun setupListeners() {
        btnAddAuthor.setOnClickListener {
            addAuthor()
        }

        btnUpdateAuthor.setOnClickListener {
            updateAuthor()
        }

        btnClearAuthor.setOnClickListener {
            clearForm()
        }
    }

    private fun addAuthor() {
        val id = etAuthorId.text.toString().toIntOrNull()
        val name = etAuthorName.text.toString()

        if (id == null || name.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val author = Author(id, name)
        authorController.agregarAutor(author)
        loadAuthors()
        clearForm()
        Toast.makeText(this, "Autor agregado exitosamente", Toast.LENGTH_SHORT).show()
    }

    private fun updateAuthor() {
        val id = etAuthorId.text.toString().toIntOrNull()
        val name = etAuthorName.text.toString()

        if (id == null || name.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val author = Author(id, name)
        authorController.agregarAutor(author)
        loadAuthors()
        clearForm()
        Toast.makeText(this, "Autor actualizado exitosamente", Toast.LENGTH_SHORT).show()
    }

    private fun loadAuthorForEditing(author: Author) {
        editingAuthorId = author.id
        etAuthorId.setText(author.id.toString())
        etAuthorName.setText(author.nombre)
    }

    private fun deleteAuthor(author: Author) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Autor")
            .setMessage("¿Estás seguro de que quieres eliminar este autor?")
            .setPositiveButton("Sí") { _, _ ->
                ApiDataManager().deleteAuthor(author.id)
                loadAuthors()
                Toast.makeText(this, "Autor eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadAuthors() {
        val authors = authorController.obtenerAutores()
        authorAdapter.updateList(authors)
    }

    private fun clearForm() {
        editingAuthorId = null
        etAuthorId.text?.clear()
        etAuthorName.text?.clear()
    }
}