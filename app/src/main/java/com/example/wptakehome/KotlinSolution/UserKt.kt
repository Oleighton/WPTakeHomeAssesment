package com.example.wptakehome.KotlinSolution


class UserKt(val name: String, val loginTime: String) {

    fun getUserName(): String {
        return name
    }

    fun getUserLoginTime(): String {
        return loginTime
    }
}
