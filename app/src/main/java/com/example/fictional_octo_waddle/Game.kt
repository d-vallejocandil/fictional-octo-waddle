package com.example.fictional_octo_waddle

data class Game(val appid: Int, val name: String, val img_icon_url: String, val id: Long = -1){
    companion object {
        const val TABLE_NAME = "Game"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_APPID = "appid"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_IMG_ICON_URL = "img_icon_url"
        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME_APPID INTEGER," +
                "$COLUMN_NAME_NAME TEXT," +
                "$COLUMN_NAME_IMG_ICON_URL TEXT)"
        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}