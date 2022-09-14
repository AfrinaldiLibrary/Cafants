package com.afrinaldi.cafants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.databinding.ActivityRegisterBinding
import com.afrinaldi.cafants.helper.LoadingDialog
import com.afrinaldi.cafants.helper.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var validation: Validation

    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        validation = Validation()
        db = Firebase.database("https://cafants-default-rtdb.asia-southeast1.firebasedatabase.app")

        inputValidation()

        binding.btnSignup.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
        binding.cvBack.setOnClickListener(this)
    }

    private fun inputValidation() {
        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidName(s.toString())){
                    binding.etName.error = "Min 3 max 50 character"
                }
            }

        })

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

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString()
                if(!validation.isPasswordSame(s.toString(), password)){
                    binding.etConfirmPassword.error = "Password tidak sama"
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_signup -> {
                register()
            }

            R.id.tv_sign_in -> {
                onBackPressed()
            }

            R.id.cv_back -> {
                onBackPressed()
            }
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val cPassword = binding.etConfirmPassword.text.toString()

        if (!validation.isValidName(name)){
            binding.etName.error = "Min 3 max 50 character"
        }

        if (!validation.isValidEmail(email)){
            binding.etEmail.error = "email tidak valid"
        }

        if (!validation.isValidPassword(password)){
            binding.etPassword.error = "password terdiri dari 1 angka, 1 lowecase, 1 uppercase"
        }

        if(!validation.isPasswordSame(cPassword, password)){
            binding.etConfirmPassword.error = "Password tidak sama"
        }

        if (validation.isValidName(name) && validation.isValidEmail(email) && validation.isValidPassword(password) && validation.isPasswordSame(cPassword, password)){
            loading.startLoading()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if (!it.isSuccessful){
                    loading.isDismiss()
                    try {
                        throw it.exception!!
                    } catch (email : FirebaseAuthUserCollisionException){
                        Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                    } catch (e : Exception){
                        Toast.makeText(this, "Gagal membuat akun, coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    val user = auth.currentUser

                    val profRef = db.reference.child("Profile")
                    val currentDb = profRef.child(user?.uid!!)
                    currentDb.child("name").setValue(name)
                    user.sendEmailVerification().addOnCompleteListener { verified ->
                        if (verified.isSuccessful){
                            loading.isDismiss()
                            Toast.makeText(this, "Akun berhasil dibuat & cek email", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                    }
                }
            }
        }
    }
}