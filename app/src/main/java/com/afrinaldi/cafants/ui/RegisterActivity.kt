package com.afrinaldi.cafants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}