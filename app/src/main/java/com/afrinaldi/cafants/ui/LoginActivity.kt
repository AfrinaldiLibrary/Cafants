package com.afrinaldi.cafants.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.databinding.ActivityLoginBinding
import com.afrinaldi.cafants.helper.Constant
import com.afrinaldi.cafants.helper.LoadingDialog
import com.afrinaldi.cafants.helper.PrefHelper
import com.afrinaldi.cafants.helper.Validation
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var prefHelper: PrefHelper
    private lateinit var validation: Validation

    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefHelper = PrefHelper(this)
        auth = FirebaseAuth.getInstance()
        validation = Validation()

        inputValidation()

        binding.tvSignUp.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
    }

    private fun inputValidation() {
        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidEmail(s.toString())){
                    binding.etEmail.error = "Email tidak valid"
                }
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!validation.isValidPassword(s.toString())){
                    binding.etPassword.error = "Terdiri dari angka, huruf kecil, huruf besar, dan min 8 character"
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_sign_up -> {
                Intent(this, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.tv_forgot_password -> {
                Intent(this, ResetPasswordActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.btn_signin -> {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (!validation.isValidEmail(email)){
            binding.etEmail.error = "email tidak valid"
        }

        if (!validation.isValidPassword(password)){
            binding.etPassword.error = "password terdiri dari 1 angka, 1 lowecase, 1 uppercase"
        }

        if (validation.isValidEmail(email) && validation.isValidPassword(password)){
            loading.startLoading()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
                    loading.isDismiss()
                    val token = auth.currentUser?.uid.toString()
                    saveSession(token)
                    Intent(this, MainActivity::class.java).also { toMainActivity ->
                        toMainActivity.putExtra(Constant.PREF_TOKEN, token)
                        startActivity(toMainActivity)
                        finish()
                    }
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                } else{
                    loading.isDismiss()
                    Toast.makeText(this, "Login gagal, periksa kembali email dan password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveSession(token: String){
        prefHelper.put(Constant.PREF_TOKEN, token)
    }

    override fun onStart() {
        super.onStart()
        if (prefHelper.getString(Constant.PREF_TOKEN) != null){
            Intent(this, MainActivity::class.java).also { toMainActivity ->
                startActivity(toMainActivity)
                finish()
            }
        }
    }
}