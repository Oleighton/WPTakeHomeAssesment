package com.example.wptakehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.user_item, users);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.user_item, parent, false);
        }

        User user = getItem(position);

        TextView userNameTextView = view.findViewById(R.id.user_name_text_view);
        userNameTextView.setText(user.getName());

        TextView loginDateTextView = view.findViewById(R.id.login_date_text_view);
        loginDateTextView.setText(user.getLoginTime());

        return view;
    }



}

