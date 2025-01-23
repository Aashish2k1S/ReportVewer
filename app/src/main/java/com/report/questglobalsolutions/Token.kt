package com.report.questglobalsolutions

import android.content.Context
import android.content.SharedPreferences

class Token(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun save(token: String) {
        with(sharedPreferences.edit()) {
            putString("TOKEN", token)
            apply()
        }
    }

    fun read(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun del() {
        with(sharedPreferences.edit()) {
            remove("TOKEN")
            apply()
        }
    }
}
