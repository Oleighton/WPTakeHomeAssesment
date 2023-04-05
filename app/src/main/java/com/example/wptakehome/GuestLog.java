package com.example.wptakehome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestLog extends AppCompatActivity {
    private ListView userListView;
    private UserAdapter userAdapter;
    private List<String> usernames = new ArrayList<>();
    private List<String> loginDateTime = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private final String PREF_NAME = "guest_log_pref";
    HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_log);
        userListView = findViewById(R.id.user_list_view);
        data = (HashMap<String, String>) getIntent().getSerializableExtra("users");
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // Restore saved state of the list
        if (savedInstanceState != null) {
            users = (List<User>) savedInstanceState.getSerializable("users");
        }
        if (users.isEmpty()) {
            // If no saved state, use intent data to initialize the list
            if (data != null) {
                Log.d("DB", data.toString());
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    users.add(new User(entry.getKey(), entry.getValue()));
                }
            }
        }
        if (data != null) {
            Log.d("DB", data.toString());
            for (Map.Entry<String, String> entry : data.entrySet()) {
                // map key is username, value is date, populate usernames and login date info
                usernames.add(entry.getKey());
                loginDateTime.add((entry.getValue()));
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("users", (Serializable) users);
    }

    private void updateUI() {
        userAdapter = new UserAdapter(getApplicationContext(), users);
        userListView.setAdapter(userAdapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userInfo", String.valueOf(data));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

}