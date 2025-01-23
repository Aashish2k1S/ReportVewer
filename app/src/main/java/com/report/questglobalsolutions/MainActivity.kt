package com.report.questglobalsolutions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.gson.Gson
import com.report.questglobalsolutions.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        username.setText("")
        password.setText("")

        val sharedToken = Token(this@MainActivity)

        binding.add1Btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://questerp.in/")
            }
            startActivity(intent)
        }

        binding.marqueeText.isSelected = true

        binding.apiBtn.setOnClickListener {
            if ( password.text.toString().trim().isEmpty() || username.text.toString().trim().isEmpty() ) {
                Toast.makeText(this@MainActivity, "Credential missing", Toast.LENGTH_SHORT).show()
                sharedToken.del()
            }
            else {
                getData(password.text.toString().trim(), username.text.toString().trim(), sharedToken)

                Handler(Looper.getMainLooper()).postDelayed({
                    val token = sharedToken.read()
                    if (!token.isNullOrEmpty()) {
                        val intent = Intent(this@MainActivity, Home::class.java)
                        startActivity(intent)
                    }
//                    else {
//                        Toast.makeText(this@MainActivity, "Failed to retrieve token", Toast.LENGTH_SHORT).show()
//                    }
                }, 1000)
            }
        }
    }

    private fun getData(password: String, username: String, tokenManager: Token) {

        val progressDialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_layout)
            .setCancelable(false)
            .create()

        progressDialog.show()

        val req = LoginBodyClass(password, username)

        RetrofitInstance.apiInterface.postLogin(req)
        .enqueue(object : Callback<LoginResponseClass> {

            override fun onResponse(
                call: Call<LoginResponseClass?>,
                response: Response<LoginResponseClass?>
            ) {
                progressDialog.dismiss()

                if (response.code() == 200 && response.body() != null) {
                    response.body()?.token?.let { tokenManager.save(it) }
                }
                else {
                    val errorBody = response.errorBody()

                    if (errorBody != null) {
                        val errorResponse = parseErrorBody(errorBody.string())
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${errorResponse.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponseClass?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@MainActivity,
                    "Failed to connect!! Please check your internet connection..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun parseErrorBody(errorBody: String): ErrorBodyClass {
        return try {
            val gson = Gson()
            gson.fromJson(errorBody, ErrorBodyClass::class.java)
        } catch (e: Exception) {
            ErrorBodyClass(message = "Unknown error", status = 0)
        }
    }

}

