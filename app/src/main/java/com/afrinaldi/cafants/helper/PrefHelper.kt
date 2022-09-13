package com.afrinaldi.cafants.helper

import android.content.Context
import android.content.SharedPreferences

class PrefHelper (context: Context){
    private val PREFS_NAME = "sharedpref"
    private var sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun clear() {
        editor.clear()
            .apply()
    }
}