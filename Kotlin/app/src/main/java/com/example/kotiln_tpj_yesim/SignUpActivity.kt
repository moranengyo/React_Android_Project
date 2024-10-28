package com.example.kotiln_tpj_yesim

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.databinding.ActivitySignUpBinding
import com.example.kotiln_tpj_yesim.dto.SignUpDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var isIdChecked = false  // Flag to ensure ID duplicate check has passed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "회원가입"

        setupEmailDomainSpinner()
        setupButtonListeners()
        setupPasswordTextWatcher()
    }

    // Set up email domain spinner
    private fun setupEmailDomainSpinner() {
        val domains = arrayOf("naver.com", "gmail.com", "daum.net", "yahoo.com")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, domains)
        binding.emailDomain.adapter = adapter
    }

    // Set up button listeners
    private fun setupButtonListeners() {
        binding.buttonCheckId.setOnClickListener { checkUserIdDuplicate() }
        binding.buttonSignUp.setOnClickListener { attemptSignUp() }
        binding.buttonCancel.setOnClickListener { finish() }

        // Reset ID status on input change
        binding.edId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) resetIdStatus() else resetIdMessage()
                isIdChecked = false  // Reset the flag when the ID changes
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Password validation and confirmation
    private fun setupPasswordTextWatcher() {
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updatePasswordStatus(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.reEdPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateConfirmPasswordStatus()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Check if the user ID is already taken
    private fun checkUserIdDuplicate() {
        val id = binding.edId.text.toString().trim()

        if (id.isEmpty()) {
            showToast("아이디를 입력해 주세요.")
            return
        }

        RetrofitClient.retrofit.checkUserIdDuplicate(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isDuplicate = response.body() ?: false
                    if (isDuplicate) {
                        updateIdStatus("이미 사용 중인 아이디 입니다.", R.color.pink)
                        isIdChecked = false  // Set the flag to false if duplicate
                    } else {
                        updateIdStatus("사용 가능한 아이디 입니다.", R.color.green)
                        isIdChecked = true  // Set the flag to true if available
                    }
                } else {
                    showToast("ID 확인 중 오류가 발생했습니다.")
                    Log.e("CheckIDError", "Response Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                showToast("네트워크 오류가 발생했습니다.")
                Log.e("CheckIDFailure", t.message.orEmpty())
            }
        })
    }

    // Attempt to sign up only if the ID has been verified
    private fun attemptSignUp() {
        if (!isIdChecked) {
            showToast("아이디 중복 확인을 먼저 해주세요.")
            return
        }

        if (!validateFields()) return

        val name = binding.edName.text.toString().trim()
        val id = binding.edId.text.toString().trim()
        val password = binding.edPassword.text.toString()
        val email = binding.edEmaill.text.toString().trim()
        val emailDomain = binding.emailDomain.selectedItem.toString()
        val totalEmail = "$email@$emailDomain"

        val dto = SignUpDto(id, password, name, totalEmail)
        performSignUp(dto)
    }

    // Perform sign-up request
    private fun performSignUp(dto: SignUpDto) {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonSignUp.isEnabled = false

        RetrofitClient.retrofit.signUp(dto).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                binding.progressBar.visibility = View.GONE
                binding.buttonSignUp.isEnabled = true

                if (response.isSuccessful) {
                    showToast("회원가입이 완료되었습니다.")
                    finish()
                } else {
                    showToast("회원가입 중 오류가 발생했습니다.")
                    Log.e("SignUpError", "Response Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.buttonSignUp.isEnabled = true
                showToast("네트워크 오류가 발생했습니다.")
                Log.e("SignUpFailure", t.message.orEmpty())
            }
        })
    }

    // Update password status
    private fun updatePasswordStatus(password: String) {
        val color = if (validatePassword(password)) R.color.green else R.color.pink
        binding.passwordRule.setTextColor(getColor(color))
    }

    // Update confirm password status
    private fun updateConfirmPasswordStatus() {
        val password = binding.edPassword.text.toString()
        val confirmPassword = binding.reEdPassword.text.toString()

        val message = if (password == confirmPassword) "비밀번호가 일치합니다." else "비밀번호가 일치하지 않습니다."
        val color = if (password == confirmPassword) R.color.green else R.color.pink

        binding.passwordConfirmStatus.setTextColor(getColor(color))
        binding.passwordConfirmStatus.text = message
    }

    // Reset ID status message
    private fun resetIdMessage() {
        binding.idStatus.text = ""
    }

    // Reset ID status to default
    private fun resetIdStatus() {
        updateIdStatus("아이디 중복 확인이 필요합니다.", R.color.gray)
    }

    // Update ID status with a message and color
    private fun updateIdStatus(message: String, colorRes: Int) {
        binding.idStatus.text = message
        binding.idStatus.setTextColor(getColor(colorRes))
    }

    // Validate input fields
    private fun validateFields(): Boolean {
        val id = binding.edId.text.toString().trim()
        val password = binding.edPassword.text.toString()
        val name = binding.edName.text.toString().trim()
        val email = binding.edEmaill.text.toString().trim()

        return when {
            id.isEmpty() || name.isEmpty() || email.isEmpty() -> {
                showToast("모든 필드를 입력해 주세요.")
                false
            }
            !validatePassword(password) -> {
                showToast("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
                false
            }
            password != binding.reEdPassword.text.toString() -> {
                showToast("비밀번호가 일치하지 않습니다.")
                false
            }
            else -> true
        }
    }

    // Validate password with regex
    private fun validatePassword(password: String): Boolean {
        val pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#\$%]).{6,}$"
        return password.matches(Regex(pattern))
    }

    // Show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
