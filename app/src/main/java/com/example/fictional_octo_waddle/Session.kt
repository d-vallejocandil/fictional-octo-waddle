package com.example.fictional_octo_waddle

import android.content.Context

class Session(context: Context) {
    val session = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    fun set(id: String) {
        val edit = session.edit()
        edit.putString("STEAM_ID", id)
        edit.apply()
    }
    fun get(): String {
        return session.getString("STEAM_ID", "")!!
    }
}