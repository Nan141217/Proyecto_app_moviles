package com.example.inicio

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import Adapters.BookAdapter
import Controllers.BookController
import Data.ApiDataManager
import Person.Author
import Person.Book
import java.io.File

class LibraryActivity2 : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var etBookId: TextInputEditText
    private lateinit var etBookTitle: TextInputEditText
    private lateinit var actvAuthor: AutoCompleteTextView
    private lateinit var switchAvailable: SwitchMaterial
    private lateinit var btnAddBook: Button
    private lateinit var btnUpdateBook: Button
    private lateinit var btnClearBook: Button
    private lateinit var btnSelectImage: Button
    private lateinit var imgBookCoverPreview: ImageView
    private lateinit var rvBooks: RecyclerView

    private lateinit var bookController: BookController
    private lateinit var bookAdapter: BookAdapter
    private var selectedAuthor: Author? = null
    private var selectedBitmap: Bitmap? = null
    private var photoUri: Uri? = null
    private var editingBookId: Int? = null

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = contentResolver.openInputStream(it)
            selectedBitmap = BitmapFactory.decodeStream(inputStream)
            imgBookCoverPreview.setImageBitmap(selectedBitmap)
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            photoUri?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)
                imgBookCoverPreview.setImageBitmap(selectedBitmap)
            }
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupToolbar()
        initController()
        setupAuthorSpinner()
        setupRecyclerView()
        setupListeners()
        loadBooks()

        // Restore state if needed
        savedInstanceState?.let {
            val uriString = it.getString(KEY_PHOTO_URI)
            if (uriString != null) {
                photoUri = Uri.parse(uriString)
                try {
                    val inputStream = contentResolver.openInputStream(photoUri!!)
                    selectedBitmap = BitmapFactory.decodeStream(inputStream)
                    imgBookCoverPreview.setImageBitmap(selectedBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        photoUri?.let {
            outState.putString(KEY_PHOTO_URI, it.toString())
        }
    }

    companion object {
        private const val KEY_PHOTO_URI = "photo_uri"
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        etBookId = findViewById(R.id.etBookId)
        etBookTitle = findViewById(R.id.etBookTitle)
        actvAuthor = findViewById(R.id.actvAuthor)
        switchAvailable = findViewById(R.id.switchAvailable)
        btnAddBook = findViewById(R.id.btnAddBook)
        btnUpdateBook = findViewById(R.id.btnUpdateBook)
        btnClearBook = findViewById(R.id.btnClearBook)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        imgBookCoverPreview = findViewById(R.id.imgBookCoverPreview)
        rvBooks = findViewById(R.id.rvBooks)
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
                startActivity(Intent(this, LibraryActivity::class.java))
                true
            }
            R.id.menu_books -> {
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
        bookController = BookController(ApiDataManager())
    }

    private fun setupAuthorSpinner() {
        val authors = ApiDataManager().getAllAuthors()
        val authorNames = authors.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, authorNames)
        actvAuthor.setAdapter(adapter)

        actvAuthor.setOnItemClickListener { _, _, position, _ ->
            selectedAuthor = authors[position]
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            mutableListOf(),
            onEditClick = { book -> loadBookForEditing(book) },
            onDeleteClick = { book -> deleteBook(book) }
        )
        rvBooks.layoutManager = LinearLayoutManager(this)
        rvBooks.adapter = bookAdapter
    }

    private fun setupListeners() {
        btnSelectImage.setOnClickListener {
            showImageSelectionDialog()
        }

        btnAddBook.setOnClickListener {
            addBook()
        }

        btnUpdateBook.setOnClickListener {
            updateBook()
        }

        btnClearBook.setOnClickListener {
            clearForm()
        }
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> checkGalleryPermissionAndOpen()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            photoFile
        )
        cameraLauncher.launch(photoUri)
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "BOOK_" + System.currentTimeMillis() + "_",
            ".jpg",
            storageDir
        )
    }

    private fun checkGalleryPermissionAndOpen() {
        when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        openGallery()
                    }
                    else -> {
                        galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
            }
            else -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        openGallery()
                    }
                    else -> {
                        galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun addBook() {
        val id = etBookId.text.toString().toIntOrNull()
        val title = etBookTitle.text.toString()
        val available = switchAvailable.isChecked

        if (id == null || title.isEmpty() || selectedAuthor == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val book = Book(id, title, selectedAuthor!!, available, selectedBitmap)
        bookController.agregarLibro(book)
        loadBooks()
        clearForm()
        Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateBook() {
        val id = etBookId.text.toString().toIntOrNull()
        val title = etBookTitle.text.toString()
        val available = switchAvailable.isChecked

        if (id == null || title.isEmpty() || selectedAuthor == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val book = Book(id, title, selectedAuthor!!, available, selectedBitmap)
        bookController.actualizarLibro(book)
        loadBooks()
        clearForm()
        Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun loadBookForEditing(book: Book) {
        editingBookId = book.id
        etBookId.setText(book.id.toString())
        etBookTitle.setText(book.titulo)
        actvAuthor.setText(book.autor?.nombre ?: "", false)
        selectedAuthor = book.autor
        switchAvailable.isChecked = book.disponible

        if (book.coverImage != null) {
            selectedBitmap = book.coverImage
            imgBookCoverPreview.setImageBitmap(selectedBitmap)
        } else {
            imgBookCoverPreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    private fun deleteBook(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete this book?")
            .setPositiveButton("Yes") { _, _ ->
                bookController.eliminarLibro(book.id)
                loadBooks()
                Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadBooks() {
        val books = bookController.obtenerLibros()
        bookAdapter.updateList(books)
    }

    private fun clearForm() {
        editingBookId = null
        etBookId.text?.clear()
        etBookTitle.text?.clear()
        actvAuthor.text?.clear()
        selectedAuthor = null
        switchAvailable.isChecked = true
        selectedBitmap = null
        imgBookCoverPreview.setImageResource(android.R.drawable.ic_menu_gallery)
    }
}

