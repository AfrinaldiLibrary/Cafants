package com.afrinaldi.cafants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.afrinaldi.cafants.databinding.ActivityLoginBinding
import com.afrinaldi.cafants.helper.Constant
import com.afrinaldi.cafants.helper.PrefHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class LoginActivity : AppCompatActivity() {
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
        binding.btnSignin.setOnClickListener{
            auth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    user = Objects.requireNonNull(it.result)?.user!!

                    user.getIdToken(true).addOnSuccessListener { result ->
                        val idToken = result.token
                        saveSession(idToken!!)

                        Log.d("token", "$idToken");
                    }
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("cekatuh", prefHelper.getString(Constant.PREF_TOKEN)?: "null")
    }

    private fun saveSession(token: String){
        prefHelper.put(Constant.PREF_TOKEN, token)
    }
}