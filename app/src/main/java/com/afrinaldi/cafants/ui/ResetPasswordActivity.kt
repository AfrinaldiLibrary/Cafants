package com.afrinaldi.cafants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.databinding.ActivityResetPasswordBinding
import com.afrinaldi.cafants.helper.LoadingDialog
import com.afrinaldi.cafants.helper.Validation
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityResetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var validation: Validation

    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        validation = Validation()

        inputValidation()

        binding.btnSendVerification.setOnClickListener(this)
        binding.cvBack.setOnClickListener(this)
    }

    private fun inputValidation() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
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
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_send_verification -> {
                reset()
            }

            R.id.cv_back -> {
                onBackPressed()
            }
        }
    }

    private fun reset() {
        val email = binding.etEmail.text.toString()
        if (validation.isValidEmail(email)){
            loading.startLoading()
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    loading.isDismiss()
                    Toast.makeText(this, "Berhasil, cek email", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    loading.isDismiss()
                    Toast.makeText(this, "Gagal, coba lagi!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.etEmail.error = "Email tidak valid"
        }
    }
}