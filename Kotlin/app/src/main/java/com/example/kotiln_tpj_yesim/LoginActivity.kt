package com.example.kotiln_tpj_yesim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.databinding.ActivityLoginBinding
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.LoginCmpltDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val dbHelper by lazy { DBHelper(this) }  // DBHelper instance
    private var loginAttempts = 0  // Track login attempts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        binding.edId.requestFocus()

        setupListeners()
    }

    // Setup button click listeners
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.btnSingUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    // Handle login attempt
    private fun attemptLogin() {
        val id = binding.edId.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()

        if (!isIdValid(id) || !isPasswordValid(password)) {
            showToast("ID와 비밀번호를 확인해 주세요.")
            return
        }

        if (loginAttempts >= 5) {
            showToast("로그인 시도가 너무 많습니다. 잠시 후 다시 시도하세요.")
            return
        }

        performLogin(id, password)
    }

    // Perform login operation
    private fun performLogin(id: String, password: String) {
        showLoading(true)

        RetrofitClient.retrofit.login(id, password).enqueue(object : Callback<LoginCmpltDto> {
            override fun onResponse(call: Call<LoginCmpltDto>, response: Response<LoginCmpltDto>) {
                showLoading(false)

                if (response.isSuccessful) {
                    response.body()?.let {
                        dbHelper.saveAuth(it.userDto, it.jwtDto)
                        goToMainActivity()
                    } ?: run {
                        showToast("응답이 올바르지 않습니다.")
                    }
                } else {
                    handleLoginFailure(response.code())
                }
            }

            override fun onFailure(call: Call<LoginCmpltDto>, t: Throwable) {
                showLoading(false)
                Log.e("LoginFailure", t.message.orEmpty())
                showToast("네트워크 오류가 발생했습니다. 다시 시도해 주세요.")
            }
        })
    }

    // Handle login failure and increment login attempts
    private fun handleLoginFailure(responseCode: Int) {
        loginAttempts++
        Log.e("LoginError", "Response Code: $responseCode")
        showToast("승인 되지 않은 아이디거나 비밀번호가 잘못되었습니다.")
    }

    // Navigate to MainActivity
    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Validate user ID
    private fun isIdValid(id: String): Boolean = id.isNotEmpty() && id.length >= 4

    // Validate password
    private fun isPasswordValid(password: String): Boolean = password.length >= 6

    // Show or hide loading indicator
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
    }

    // Show toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
