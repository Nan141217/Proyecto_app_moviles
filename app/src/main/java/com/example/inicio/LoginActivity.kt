package com.example.inicio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import Network.Models.LoginRequest
import Network.RetrofitClient
import Util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToHome()
            return
        }

        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                login(username, password)
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            etUsername.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun login(username: String, password: String) {
        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = LoginRequest(username, password)
                val response = RetrofitClient.userService.login(request)

                withContext(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        if (apiResponse.responseCode == 200 && apiResponse.data != null) {
                            val user = apiResponse.data
                            sessionManager.saveSession(
                                user.id,
                                user.username,
                                user.name,
                                user.email
                            )
                            Toast.makeText(
                                this@LoginActivity,
                                "Welcome ${user.name}!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToHome()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                apiResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !show
        etUsername.isEnabled = !show
        etPassword.isEnabled = !show
    }

    private fun navigateToHome() {
        val intent = Intent(this, LibraryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
