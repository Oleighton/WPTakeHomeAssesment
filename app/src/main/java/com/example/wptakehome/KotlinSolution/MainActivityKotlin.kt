package com.example.wptakehome.KotlinSolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Build
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.wptakehome.R


class MainActivityKotlin : AppCompatActivity() {
    private lateinit var visitorCountVal: TextView
    lateinit var welcomeBack: TextView
    private lateinit var guestLog: Button
    private lateinit var visitorCheckIn: Button
    private var data = HashMap<String, String>()
    private var userCounter = 0
    lateinit var sharedPreferences: SharedPreferences
    private val USER_DATA_KEY = "user_data"
    private var userInfo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        visitorCountVal = findViewById(R.id.visitorCount)
        welcomeBack = findViewById(R.id.welcomBackLog)
        guestLog = findViewById(R.id.guestLogBTN)
        visitorCheckIn = findViewById(R.id.visitorCheckInBTN)
        sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE)

        // set listener for visitor check in
        visitorCheckIn.setOnClickListener {
            welcomeUser()
        }

        // view guest log
        guestLog.setOnClickListener {
            val intent = Intent(applicationContext, GuestLogKotlin::class.java)
            data = UserStoredInfoKt.getInstance().getData()
            intent.putExtra("users", data)
            startActivity(intent)
        }
    }

    /**
     * Displays an AlertDialog prompting the user to enter their name.
     * If the user's name has not been previously entered, they will be added to the user data and a welcome message will be displayed.
     * If the user's name has been previously entered, a welcome back message will be displayed.
     * User data is stored using SharedPreferences.
     */
    private fun welcomeUser() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welcome!")
        // Set up the layout for the AlertDialog
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.visitor_details, null)
        builder.setView(dialogView)

        builder.setPositiveButton("Submit") { dialog, _ ->
            // get the textview from the layout inflater
            val usersName = dialogView.findViewById<TextView>(R.id.usersNameValue)
            // convert it to a string
            userInfo = usersName.text.toString()
            // strip whitespace and convert to lowercase to ensure no duplicates will be added to the guest book
            val strippedString = userInfo.trim().replace("\\s+".toRegex(), "").toLowerCase()
            // if the input is left empty, alert the user to enter a valid name
            if (userInfo.isEmpty()) {
                showToast("Please enter a valid name")
                return@setPositiveButton
            }
            // check if user has already visited, if so welcome them back
            if (UserStoredInfoKt.getInstance().containsUser(strippedString)) {
                // set appropriate welcome back message
                welcomeBack.text = "Welcome back, $userInfo!"
            } else {
                // add new user to the guest book
                returningGuest(strippedString, userInfo)
            }
        }

        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun returningGuest(strippedString: String, userInfo: String) {
        var newUser: UserKt? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create a new user
            newUser = UserKt(strippedString, LocalDateTime.now().toString())
            // add new user
            UserStoredInfoKt.getInstance().addUser(newUser)
            // increment visitor count
            userCounter++
            visitorCountVal.text = userCounter.toString()
            // set thank you for visiting message
            showWelcomeMessage(userInfo)
            // save user data in shared preferences
            saveUserDataSharedPreferences()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun saveUserDataSharedPreferences() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(UserStoredInfoKt.getInstance().getData())
        editor.putString(USER_DATA_KEY, json)
        editor.putInt("userCounter", userCounter)
        editor.apply()
    }


    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putInt("userCounter", userCounter)
        userInfo?.let { editor.putString("userName", it) }
        saveUserDataSharedPreferences()
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val json = sharedPreferences.getString(USER_DATA_KEY, null)
        val name = sharedPreferences.getString("userName", userInfo)
        if (name != null) {
            showWelcomeMessage(name)
        }
        if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            data = gson.fromJson(json, type)
            UserStoredInfoKt.getInstance().setData(data)
            userCounter = sharedPreferences.getInt("userCounter", 0)
            visitorCountVal.text = userCounter.toString()
        }
    }

    fun showWelcomeMessage(name: String) {
        welcomeBack.text = "Thank you for checking in, $name!"
    }
}