package com.example.fictional_octo_waddle

data class Achievement(val name: String, val hidden: Int, val description: String, val icon: String, val icongray: String, val achieved: Int, val game: Game, val id: Long = -1) {
    companion object {
        const val TABLE_NAME = "Game"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_HIDDEN = "hidden"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_ICON = "icon"
        const val COLUMN_NAME_ICON_GRAY = "icongray"
        const val COLUMN_NAME_ACHIEVED = "achieved"
        const val COLUMN_NAME_GAME = "gameid"
        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME_NAME TEXT," +
                "$COLUMN_NAME_HIDDEN INTEGER," +
                "$COLUMN_NAME_DESCRIPTION TEXT," +
                "$COLUMN_NAME_ICON TEXT," +
                "$COLUMN_NAME_ICON_GRAY TEXT," +
                "$COLUMN_NAME_ACHIEVED INTEGER," +
                "FOREIGN KEY($COLUMN_NAME_GAME) REFERENCES ${Game.TABLE_NAME}(${Game.COLUMN_NAME_ID}) ON DELETE CASCADE)"
        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}