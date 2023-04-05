package com.example.wptakehome.KotlinSolution


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.wptakehome.R

class UserAdapterKt(context: Context, users: List<UserKt>) :
        ArrayAdapter<UserKt>(context, R.layout.user_item, users) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.user_item, parent, false)
        }

        val user = getItem(position)

        val userNameTextView = view!!.findViewById<TextView>(R.id.user_name_text_view)
        userNameTextView.text = user?.getUserName()

        val loginDateTextView = view.findViewById<TextView>(R.id.login_date_text_view)
        loginDateTextView.text = user?.getUserLoginTime()

        return view
    }
}
