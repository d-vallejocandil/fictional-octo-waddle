package com.example.fictional_octo_waddle

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class AchievementDAO(val context: Context) {
    lateinit var db: SQLiteDatabase
    val dao = GameDAO(context)
    fun open() {
        db = DatabaseManager(context).writableDatabase
    }
    fun close() {
        db.close()
    }
    fun values(achievement: Achievement): ContentValues {
        val values = ContentValues()
        values.put(Achievement.COLUMN_NAME_NAME, achievement.name)
        values.put(Achievement.COLUMN_NAME_HIDDEN, achievement.hidden)
        values.put(Achievement.COLUMN_NAME_DESCRIPTION, achievement.description)
        values.put(Achievement.COLUMN_NAME_ICON, achievement.icon)
        values.put(Achievement.COLUMN_NAME_ICON_GRAY, achievement.icongray)
        values.put(Achievement.COLUMN_NAME_ACHIEVED, achievement.achieved)
        values.put(Achievement.COLUMN_NAME_GAME, achievement.game.id)
        return values
    }
    fun cursor(cursor: Cursor): Achievement {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_NAME))
        val hidden = cursor.getInt(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_HIDDEN))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_DESCRIPTION))
        val icon = cursor.getString(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_ICON))
        val icongray = cursor.getString(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_ICON_GRAY))
        val achieved = cursor.getInt(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_ACHIEVED))
        val gameid = cursor.getInt(cursor.getColumnIndexOrThrow(Achievement.COLUMN_NAME_GAME))
        val game = dao.get(gameid)!!
        return Achievement(id, name, hidden, description, icon, icongray, achieved, game)
    }
    fun insert(achievement: Achievement) {
        open()
        val values = values(achievement)
        try {
            db.insertOrThrow(Achievement.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
    }
    fun delete(achievement: Achievement) {

    }
    fun delete(id: Int) {
        open()
        try {
            db.delete(Achievement.TABLE_NAME, "${Achievement.COLUMN_NAME_GAME} = $id", null)
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
    }
    fun update(achievement: Achievement) {

    }
    fun get(id: Int): List<Achievement> {
        open()
        val achievements: MutableList<Achievement> = mutableListOf()
        val cursor = db.query(Achievement.TABLE_NAME, null, "${Achievement.COLUMN_NAME_GAME} = $id", null, null, null, null)
        try {
            while (cursor.moveToNext()) {
                achievements.add(cursor(cursor))
            }
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
        return achievements
    }
}