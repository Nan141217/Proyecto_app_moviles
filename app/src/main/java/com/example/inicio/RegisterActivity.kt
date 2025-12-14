package com.example.inicio

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import Network.Models.RegisterRequest
import Network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var etLastname: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etName = findViewById(R.id.etName)
        etLastname = findViewById(R.id.etLastname)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val name = etName.text.toString().trim()
            val lastname = etLastname.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(username, name, email, password)) {
                register(username, name, lastname, email, password)
            }
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        username: String,
        name: String,
        email: String,
        password: String
    ): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            etUsername.requestFocus()
            return false
        }

        if (name.isEmpty()) {
            etName.error = "Name is required"
            etName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Invalid email format"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty() || password.length < 4) {
            etPassword.error = "Password must be at least 4 characters"
            etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun register(
        username: String,
        name: String,
        lastname: String,
        email: String,
        password: String
    ) {
        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = RegisterRequest(username, password, name, lastname, email)
                val response = RetrofitClient.userService.register(request)

                withContext(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        if (apiResponse.responseCode == 201 && apiResponse.data != null) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration successful! Please login",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                apiResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnRegister.isEnabled = !show
        etUsername.isEnabled = !show
        etName.isEnabled = !show
        etLastname.isEnabled = !show
        etEmail.isEnabled = !show
        etPassword.isEnabled = !show
    }
}
