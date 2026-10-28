package com.example.fictional_octo_waddle

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Game.SQL_CREATE)
        db.execSQL(Achievement.SQL_CREATE)
    }
    fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(Achievement.SQL_DROP)
        db.execSQL(Game.SQL_DROP)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
        onDestroy(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys = ON;")
    }
    companion object {
        const val DATABASE_NAME = "fictional_octo_waddle.db"
        const val DATABASE_VERSION = 1
    }
}