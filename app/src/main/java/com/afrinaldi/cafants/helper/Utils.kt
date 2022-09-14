package com.afrinaldi.cafants.helper

import android.app.Activity
import android.app.AlertDialog
import android.util.Patterns
import com.afrinaldi.cafants.R
import java.util.regex.Pattern

class Validation {

    fun isValidName(name: String?) : Boolean {
        val passwordREGEX = Pattern.compile("^.{3,50}$")
        return passwordREGEX.matcher(name.toString()).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String?) : Boolean {
        val passwordREGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")
        return passwordREGEX.matcher(password.toString()).matches()
    }

    fun isPasswordSame(password1: String?, password2: String?) : Boolean {
        return password1.equals(password2)
    }
}

class LoadingDialog(private val mActivity: Activity) {
    private lateinit var isdialog: AlertDialog

    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_dialog,null)

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun isDismiss(){
        if(::isdialog.isInitialized){
            isdialog.dismiss()
        }
    }
}