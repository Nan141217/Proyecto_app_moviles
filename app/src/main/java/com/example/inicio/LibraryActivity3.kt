package com.example.inicio

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import Adapters.LoanAdapter
import Controllers.LoanController
import Data.ApiDataManager
import Person.Book
import Person.Loan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LibraryActivity3 : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var etLoanId: TextInputEditText
    private lateinit var actvBook: AutoCompleteTextView
    private lateinit var etBorrower: TextInputEditText
    private lateinit var etLoanDate: TextInputEditText
    private lateinit var switchReturned: SwitchMaterial
    private lateinit var btnAddLoan: Button
    private lateinit var btnUpdateLoan: Button
    private lateinit var btnClearLoan: Button
    private lateinit var rvLoans: RecyclerView

    private lateinit var loanController: LoanController
    private lateinit var loanAdapter: LoanAdapter
    private var selectedBook: Book? = null
    private var selectedDate: Date = Date()
    private var editingLoanId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupToolbar()
        initController()
        setupBookSpinner()
        setupRecyclerView()
        setupListeners()
        loadLoans()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        etLoanId = findViewById(R.id.etLoanId)
        actvBook = findViewById(R.id.actvBook)
        etBorrower = findViewById(R.id.etBorrower)
        etLoanDate = findViewById(R.id.etLoanDate)
        switchReturned = findViewById(R.id.switchReturned)
        btnAddLoan = findViewById(R.id.btnAddLoan)
        btnUpdateLoan = findViewById(R.id.btnUpdateLoan)
        btnClearLoan = findViewById(R.id.btnClearLoan)
        rvLoans = findViewById(R.id.rvLoans)
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
                startActivity(Intent(this, LibraryActivity2::class.java))
                true
            }
            R.id.menu_loans -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initController() {
        loanController = LoanController(ApiDataManager())
    }

    private fun setupBookSpinner() {
        val books = ApiDataManager().getAllBooks()
        val bookTitles = books.map { it.titulo }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bookTitles)
        actvBook.setAdapter(adapter)

        actvBook.setOnItemClickListener { _, _, position, _ ->
            selectedBook = books[position]
        }
    }

    private fun setupRecyclerView() {
        loanAdapter = LoanAdapter(
            mutableListOf(),
            onEditClick = { loan -> loadLoanForEditing(loan) },
            onDeleteClick = { loan -> deleteLoan(loan) }
        )
        rvLoans.layoutManager = LinearLayoutManager(this)
        rvLoans.adapter = loanAdapter
    }

    private fun setupListeners() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etLoanDate.setText(dateFormat.format(selectedDate))

        etLoanDate.setOnClickListener {
            showDatePicker()
        }

        btnAddLoan.setOnClickListener {
            addLoan()
        }

        btnUpdateLoan.setOnClickListener {
            updateLoan()
        }

        btnClearLoan.setOnClickListener {
            clearForm()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                etLoanDate.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun addLoan() {
        val id = etLoanId.text.toString().toIntOrNull()
        val borrower = etBorrower.text.toString()
        val returned = switchReturned.isChecked

        if (id == null || borrower.isEmpty() || selectedBook == null) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val loan = Loan(id, selectedBook!!, borrower, selectedDate, returned)
        ApiDataManager().addLoan(loan)
        loadLoans()
        clearForm()
        Toast.makeText(this, "Préstamo agregado exitosamente", Toast.LENGTH_SHORT).show()
    }

    private fun updateLoan() {
        val id = etLoanId.text.toString().toIntOrNull()
        val borrower = etBorrower.text.toString()
        val returned = switchReturned.isChecked

        if (id == null || borrower.isEmpty() || selectedBook == null) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val loan = Loan(id, selectedBook!!, borrower, selectedDate, returned)
        ApiDataManager().updateLoan(loan)
        loadLoans()
        clearForm()
        Toast.makeText(this, "Préstamo actualizado exitosamente", Toast.LENGTH_SHORT).show()
    }

    private fun loadLoanForEditing(loan: Loan) {
        editingLoanId = loan.id
        etLoanId.setText(loan.id.toString())
        actvBook.setText(loan.libro?.titulo ?: "", false)
        selectedBook = loan.libro
        etBorrower.setText(loan.prestatario)
        selectedDate = loan.fecha
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etLoanDate.setText(dateFormat.format(selectedDate))
        switchReturned.isChecked = loan.devuelto
    }

    private fun deleteLoan(loan: Loan) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Préstamo")
            .setMessage("¿Estás seguro de que quieres eliminar este préstamo?")
            .setPositiveButton("Sí") { _, _ ->
                ApiDataManager().deleteLoan(loan.id)
                loadLoans()
                Toast.makeText(this, "Préstamo eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadLoans() {
        val loans = loanController.obtenerPrestamos()
        loanAdapter.updateList(loans)
    }

    private fun clearForm() {
        editingLoanId = null
        etLoanId.text?.clear()
        actvBook.text?.clear()
        selectedBook = null
        etBorrower.text?.clear()
        selectedDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etLoanDate.setText(dateFormat.format(selectedDate))
        switchReturned.isChecked = false
    }
}