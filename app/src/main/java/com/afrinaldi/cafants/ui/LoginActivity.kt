package com.afrinaldi.cafants.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.databinding.ActivityLoginBinding
import com.afrinaldi.cafants.helper.Constant
import com.afrinaldi.cafants.helper.PrefHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefHelper = PrefHelper(this)
        auth = FirebaseAuth.getInstance()

        binding.tvSignUp.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_sign_up -> {
                Intent(this, RegisterActivity::class.java).also {
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

        if (!isValidEmail(email)){
            binding.etEmail.error = "email tidak valid"
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
        }

        if (!isValidPassword(password)){
            Log.d("cek", password)
            binding.etPassword.error = "password terdiri dari 1 angka, 1 lowecase, 1 uppercase"
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
        }

        if (isValidEmail(email) && isValidPassword(password)){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
//                        user = Objects.requireNonNull(it.result)?.user!!
//
//                        user.getIdToken(true).addOnSuccessListener { result ->
//                            val idToken = result.token
//                            saveSession(idToken!!)
//
//                            Log.d("token", "$idToken");
//                        }
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun isValidPassword(password: String?) : Boolean {
        val passwordREGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}$")
        return passwordREGEX.matcher(password.toString()).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun saveSession(token: String){
        prefHelper.put(Constant.PREF_TOKEN, token)
    }

    override fun onStart() {
        super.onStart()

    }
}
