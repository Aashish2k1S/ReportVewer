package com.report.questglobalsolutions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.report.questglobalsolutions.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : ComponentActivity() {

    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedToken = Token(this@Profile)
        val token = sharedToken.read()


        if (token.isNullOrEmpty()) {
            Toast.makeText(this@Profile, "Sorry but the session is invalid!!", Toast.LENGTH_SHORT).show()
            sharedToken.del()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            binding.pageTitle.text = ""
            binding.homeTxt.setTextColor(Color.parseColor("#dadada"))
            binding.homeIcon.setColorFilter(Color.parseColor("#dadada"))
            binding.profileTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.profileIcon.setColorFilter(Color.parseColor("#FFFFFF"))
            binding.recyclerView.layoutManager = LinearLayoutManager(this)

            getData(token)
        }


        binding.add1Btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://questerp.in/")
            }
            startActivity(intent)
        }

        binding.marqueeText.isSelected = true

        binding.refreshImg.setOnClickListener {
            val refreshedToken = sharedToken.read()
            if (!refreshedToken.isNullOrEmpty()) {
                getData(refreshedToken)
            }
            else {
                Toast.makeText(this@Profile, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
                sharedToken.del()
                val intent = Intent(this@Profile, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        binding.profile.setOnClickListener {
            Toast.makeText(this, "Your on the Profile page", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getData(token: String) {
        val progressDialog = AlertDialog.Builder(this@Profile)
            .setView(R.layout.progress_layout)
            .setCancelable(false)
            .create()

        progressDialog.show()

        val req = "Bearer $token"

        RetrofitInstance.apiInterface.getProfile(req)
            .enqueue(object : Callback<ProfileBodyClass> {

                override fun onResponse(
                    call: Call<ProfileBodyClass?>,
                    response: Response<ProfileBodyClass?>
                ) {
                    progressDialog.dismiss()

                    val data =
                        if (response.code() == 200 && response.body() != null) {
                        response.body()!!.user
                    }
                        else {
                        if (response.code() != 300) handleSessionInvalid()

                        val errorMessage = response.errorBody()?.string()?.let {
                            parseErrorBody(it).message
                        } ?: "Something went wrong!!"

                        Toast.makeText(this@Profile, "Error: $errorMessage", Toast.LENGTH_SHORT).show()

                        User("", 0, 0, "", "", "", "No Data Found!!")

                    }

                    if (data.username == "No Data Found!!") {
                        Toast.makeText(this@Profile, "No Data Found!!", Toast.LENGTH_SHORT).show()
                    }
                    updateRecyclerView(data)
                }
                override fun onFailure(call: Call<ProfileBodyClass?>, t: Throwable) {

                    progressDialog.dismiss()

                    Toast.makeText(this@Profile, "Failed to connect! Please check your internet connection.", Toast.LENGTH_SHORT).show()

                    val data = User("", 0, 0, "", "", "", "404 Something went wrong!!")
                    updateRecyclerView(data)
                }
            })
    }

    private fun updateRecyclerView(data: User) {
        val adapter = ProfileBodyAdapter(data)
        binding.recyclerView.adapter = adapter
    }

    private fun parseErrorBody(errorBody: String): ErrorBodyClass {
        return try {
            val gson = Gson()
            gson.fromJson(errorBody, ErrorBodyClass::class.java)
        } catch (e: Exception) {
            ErrorBodyClass(message = "Unknown error", status = 0)
        }
    }

    private fun handleSessionInvalid() {
        runOnUiThread {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
            Token(this@Profile).del()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}