package com.example.wptakehome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;

/**

 This class represents the main activity of the app.
 The main activity displays two buttons:
 Visitor check-in button to allow visitors to check in to the app
 Guest log button to view all visitor data that has been stored
 When a visitor checks in, an AlertDialog is displayed prompting the visitor to enter their name.
 If the visitor's name has not been previously entered, they will be added to the user data and a welcome message will be displayed.
 If the visitor's name has been previously entered, a welcome back message will be displayed.
 User data is stored using SharedPreferences and can be accessed by the GuestLog activity.
 */

public class MainActivityJava extends AppCompatActivity {
    private TextView visitorCountVal;
    public TextView welcomeBack;
    private Button guestLog, visitorCheckIn;
    HashMap<String, String> data = new HashMap<>();
    private int userCounter;
    public SharedPreferences sharedPreferences;
    private static final String USER_DATA_KEY = "user_data";
    private String userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        visitorCountVal = findViewById(R.id.visitorCount);
        welcomeBack = findViewById(R.id.welcomBackLog);
        guestLog = findViewById(R.id.guestLogBTN);
        visitorCheckIn = findViewById(R.id.visitorCheckInBTN);
        sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        // set listener for visitor check in
        visitorCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeUser();
            }
        });
        // view guest log
        guestLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GuestLog.class);
                data = UserStoredInfo.getInstance().getData();
                intent.putExtra("users", data);
                startActivity(intent);
            }
        });

    }
    /**
     * Displays an AlertDialog prompting the user to enter their name.
     * If the user's name has not been previously entered, they will be added to the user data and a welcome message will be displayed.
     * If the user's name has been previously entered, a welcome back message will be displayed.
     * User data is stored using SharedPreferences.
     */
    public void welcomeUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityJava.this);
        builder.setTitle("Welcome!");
        // Set up the layout for the AlertDialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.visitor_details, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get the textview from the layout inflater
                TextView usersName = dialogView.findViewById(R.id.usersNameValue);
                // convert it to a string
                userInfo = usersName.getText().toString();
                // strip whitespace and convert to lowercase to ensure no duplicates will be added to the guest book
                String strippedString = userInfo.trim().replaceAll("\\s+", "").toLowerCase();
                // if the input is left empty, alert the user to enter a valid name
                if (userInfo.isEmpty()) {
                    showToast("Please enter a valid name");
                    return;
                }
                // check if user has already visited, if so welcome them back
                if (UserStoredInfo.getInstance().containsUser(strippedString)) {
                    // set appropriate welcome back message
                    welcomeBack.setText("Welcome back, " + userInfo + "!");
                } else {
                    // add new user to the guest book
                    returningGuest(strippedString, userInfo);
                }

            }

        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void returningGuest(String strippedString, String userInfo){
        User newUser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create a new user
            newUser = new User(strippedString, LocalDateTime.now().toString());
            // add mew user
            UserStoredInfo.getInstance().addUser(newUser);
            // increment visitor count
            userCounter++;
            visitorCountVal.setText(String.valueOf(userCounter));
            // set  thank you for visiting message
            showWelcomeMessage(userInfo);
            // save user data in shared preferences
            saveUserDataSharedPreferences();

         }

    }

    public void saveUserDataSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(UserStoredInfo.getInstance().getData());
        editor.putString(USER_DATA_KEY, json);
        editor.putInt("userCounter", userCounter);
        editor.apply();

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userCounter", userCounter);
        if (userInfo != null){
            editor.putString("userName", userInfo);
        }
        saveUserDataSharedPreferences();
        editor.apply();

    }


    @Override
    protected void onResume() {
        super.onResume();
        String json = sharedPreferences.getString(USER_DATA_KEY, null);
        String name = sharedPreferences.getString("userName",userInfo);
        if (name != null){
            showWelcomeMessage(name);
        }
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            data = gson.fromJson(json, type);
            UserStoredInfo.getInstance().setData(data);
            userCounter = sharedPreferences.getInt("userCounter", 0);
            visitorCountVal.setText(String.valueOf(userCounter));

        }
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void showWelcomeMessage(String name) {
        welcomeBack.setText("Thank you for checking in, " + name + "!");
    }



}