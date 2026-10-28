package com.example.fictional_octo_waddle

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class GameDAO(val context: Context) {
    lateinit var db: SQLiteDatabase
    fun open() {
        db = DatabaseManager(context).writableDatabase
    }
    fun close() {
        db.close()
    }
    fun values(game: Game): ContentValues {
        val values = ContentValues()
        values.put(Game.COLUMN_NAME_APPID, game.appid)
        values.put(Game.COLUMN_NAME_NAME, game.name)
        values.put(Game.COLUMN_NAME_IMG_ICON_URL, game.img_icon_url)
        return values
    }
    fun cursor(cursor: Cursor): Game {
        val appid = cursor.getInt(cursor.getColumnIndexOrThrow(Game.COLUMN_NAME_APPID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_NAME_NAME))
        val img_icon_url = cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_NAME_IMG_ICON_URL))
        return Game(appid, name, img_icon_url)
    }
    fun insert(game: Game) {
        open()
        val values = values(game)
        try {
            db.insertOrThrow(Game.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
    }
    fun delete(game: Game) {

    }
    fun update(game: Game) {

    }
    fun get(id: Int): Game? {
        open()
        var game: Game? = null
        val cursor = db.query(Game.TABLE_NAME, null, "${Game.COLUMN_NAME_APPID} = $id", null, null, null, null)
        try {
            while (cursor.moveToNext()) {
                game = cursor(cursor)
            }
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
        return game
    }
    fun get(): List<Game> {
        open()
        var games: MutableList<Game> = mutableListOf()
        val cursor = db.query(Game.TABLE_NAME, null, null, null, null, null, null)
        try {
            while (cursor.moveToNext()) {
                games.add(cursor(cursor))
            }
        } catch (e: Exception) {
            Log.e("DB", e.toString())
        } finally {
            close()
        }
        return games
    }
}