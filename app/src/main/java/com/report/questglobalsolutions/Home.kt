package com.report.questglobalsolutions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.graphics.Color
import android.net.Uri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.report.questglobalsolutions.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : ComponentActivity() {

    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedToken = Token(this@Home)
        val token = sharedToken.read()

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sorry but the session is invalid!!", Toast.LENGTH_SHORT).show()
            sharedToken.del()
            val intent = Intent(this@Home, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            binding.pageTitle.text = ""
            binding.homeTxt.setTextColor(Color.parseColor("#FFFFFF"))
            binding.homeIcon.setColorFilter(Color.parseColor("#FFFFFF"))
            binding.profileTxt.setTextColor(Color.parseColor("#dadada"))
            binding.profileIcon.setColorFilter(Color.parseColor("#dadada"))
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
            } else {
                Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
                sharedToken.del()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.home.setOnClickListener {
            Toast.makeText(this, "Your on the Home page", Toast.LENGTH_SHORT).show()
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun getData(token: String) {
        val progressDialog = AlertDialog.Builder(this@Home)
            .setView(R.layout.progress_layout)
            .setCancelable(false)
            .create()

        progressDialog.show()

        val req = "Bearer $token"

        RetrofitInstance.apiInterface.getHome(req)
            .enqueue(object : Callback<HomeBodyClass> {
                override fun onResponse(
                    call: Call<HomeBodyClass?>,
                    response: Response<HomeBodyClass?>
                ) {
                    progressDialog.dismiss()

                    //#region
//                    val jsonData = """
//                        {
//                            "data": [
//                                {
//                                    "appreportId": "1",
//                                    "report": "Outstanding Details",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-14"
//                                },
//                                {
//                                    "appreportId": "2",
//                                    "report": "Outstanding Summary",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-13"
//                                },
//                                {
//                                    "appreportId": "3",
//                                    "report": "Order Pending",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-12"
//                                },
//                                {
//                                    "appreportId": "4",
//                                    "report": "Sales Report",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-11"
//                                },
//                                {
//                                    "appreportId": "5",
//                                    "report": "Project Milestone Update",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-10"
//                                },
//                                {
//                                    "appreportId": "1",
//                                    "report": "Outstanding Details",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-14"
//                                },
//                                {
//                                    "appreportId": "2",
//                                    "report": "Outstanding Summary",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-13"
//                                },
//                                {
//                                    "appreportId": "3",
//                                    "report": "Order Pending",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-12"
//                                },
//                                {
//                                    "appreportId": "4",
//                                    "report": "Sales Report",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-11"
//                                },
//                                {
//                                    "appreportId": "5",
//                                    "report": "Project Milestone Update",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-10"
//                                },
//                                {
//                                    "appreportId": "1",
//                                    "report": "Outstanding Details",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-14"
//                                },
//                                {
//                                    "appreportId": "2",
//                                    "report": "Outstanding Summary",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-13"
//                                },
//                                {
//                                    "appreportId": "3",
//                                    "report": "Order Pending",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-12"
//                                },
//                                {
//                                    "appreportId": "4",
//                                    "report": "Sales Report",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-11"
//                                },
//                                {
//                                    "appreportId": "5",
//                                    "report": "Project Milestone Update",
//                                    "downloadUrl": "https://drive.google.com/file/d/1Lp0ZoCuU5gfPcu-rkdqOOHfCrWsvUjBY/view?usp=sharing",
//                                    "addDate": "2025-01-10"
//                                }
//                            ],
//                            "status": 200
//                        }
//                    """
//
//                    val gson = Gson()
//                    val data = gson.fromJson(jsonData, HomeBodyClass::class.java)
                    //#endregion

                    val data = if (response.code() == 200 && response.body() != null) {
                        response.body()!!.apply {
                            if (this.data.isEmpty()) {
                                this.data = listOf(Data("", "0", "", "No Data Found!!"))
                                Toast.makeText(this@Home, "No Data Found!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        if (response.code() != 300) handleSessionInvalid()

                        val errorMessage = response.errorBody()?.string()?.let {
                            parseErrorBody(it).message
                        } ?: "Something went wrong!!"

                        Toast.makeText(this@Home, "Error: $errorMessage", Toast.LENGTH_SHORT).show()

                        HomeBodyClass(
                            data = listOf(Data("", "0", "", errorMessage)),
                            status = response.code()
                        )
                    }
                    updateRecyclerView(data.data)
                }
                override fun onFailure(call: Call<HomeBodyClass?>, t: Throwable) {
                    progressDialog.dismiss()

                    Toast.makeText(this@Home, "Failed to connect! Please check your internet connection.", Toast.LENGTH_SHORT).show()

                    val data = HomeBodyClass(
                        data = listOf(Data("", "0", "", "404 Something went wrong!!")),
                        status = 404
                    )

                    updateRecyclerView(data.data)
                }
            })
    }

    private fun updateRecyclerView(data: List<Data>) {
        binding.recyclerView.layoutManager = GridLayoutManager(this@Home, 3, RecyclerView.VERTICAL, false)
        val adapter = HomeBodyAdapter(data)
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
            Token(this@Home).del()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}