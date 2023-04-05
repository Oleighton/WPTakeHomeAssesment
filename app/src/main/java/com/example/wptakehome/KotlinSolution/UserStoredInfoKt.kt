package com.example.wptakehome.KotlinSolution


import java.util.HashMap

object UserStoredInfoKt {
    private var sInstance: UserStoredInfoKt? = null
    private var mData: HashMap<String, String> = HashMap()

    fun getInstance(): UserStoredInfoKt {
        if (sInstance == null) {
            sInstance = UserStoredInfoKt
        }
        return sInstance!!
    }

    fun getData(): HashMap<String, String> {
        return mData
    }

    fun addUser(user: UserKt) {
        mData[user.name] = user.loginTime
    }

    fun containsUser(username: String): Boolean {
        val data: Map<String, String> = UserStoredInfoKt.getInstance().getData()
        return data.containsKey(username)
    }

    fun setData(newData: HashMap<String, String>) {
        mData = newData
    }
}
