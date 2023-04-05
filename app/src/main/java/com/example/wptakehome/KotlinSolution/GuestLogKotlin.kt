package com.example.wptakehome.KotlinSolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.wptakehome.R

import android.content.SharedPreferences
import android.util.Log
import android.widget.ListView

import com.google.gson.Gson
import java.io.Serializable
import kotlin.collections.HashMap

class GuestLogKotlin : AppCompatActivity() {
    private lateinit var userListView: ListView
    private lateinit var userAdapter: UserAdapterKt
    private var usernames = mutableListOf<String>()
    private var loginDateTime = mutableListOf<String>()
    private var users = mutableListOf<UserKt>()
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "guest_log_pref"
    private var data = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_log)
        userListView = findViewById(R.id.user_list_view)
        data = intent.getSerializableExtra("users") as HashMap<String, String>
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        // Restore saved state of the list
        savedInstanceState?.let {
            users = it.getSerializable("users") as MutableList<UserKt>
        }
        if (users.isEmpty()) {
            // If no saved state, use intent data to initialize the list
            data?.let {
                Log.d("DB", it.toString())
                for ((key, value) in it) {
                    users.add(UserKt(key, value))
                }
            }
        }
        data?.let {
            Log.d("DB", it.toString())
            for ((key, value) in it) {
                // map key is username, value is date, populate usernames and login date info
                usernames.add(key)
                loginDateTime.add(value)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("users", users as Serializable)
    }

    private fun updateUI() {
        userAdapter = UserAdapterKt(applicationContext, users)
        userListView.adapter = userAdapter
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putString("userInfo", Gson().toJson(data))
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

}