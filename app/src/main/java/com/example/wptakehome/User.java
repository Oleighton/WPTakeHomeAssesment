package com.example.wptakehome;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class User {
        private String name;
        private String loginTime;

        public User(String name, String loginTime) {
            this.name = name;
            this.loginTime = loginTime;
        }

        public String getName() {
            return name;
        }

        public String getLoginTime() {
            return loginTime;
        }



}
