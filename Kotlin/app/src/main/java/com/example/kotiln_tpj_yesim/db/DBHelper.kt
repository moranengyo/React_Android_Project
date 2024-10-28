package com.example.kotiln_tpj_yesim.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.kotiln_tpj_yesim.dto.JwtDto
import com.example.kotiln_tpj_yesim.dto.UserDto

class DBHelper(context: Context) : SQLiteOpenHelper(context, "AUTH_DB", null, 1) {

    companion object {
        const val TABLE_NAME = "AUTH"
        const val COLUMN_SEQ = "user_seq"
        const val COLUMN_ID = "user_id"
        const val COLUMN_NAME = "user_name"
        const val COLUMN_ROLE = "user_role"
        const val COLUMN_EMAIL = "user_email"
        const val COLUMN_ACCESS_TOKEN = "access_token"
        const val COLUMN_REFRESH_TOKEN = "refresh_token"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SEQ BIGINT,
                $COLUMN_ID VARCHAR(100),
                $COLUMN_NAME VARCHAR(25),
                $COLUMN_ROLE VARCHAR(20),
                $COLUMN_EMAIL VARCHAR(100),
                $COLUMN_ACCESS_TOKEN VARCHAR(1000),
                $COLUMN_REFRESH_TOKEN VARCHAR(1000)
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun deleteAllAuth() {
        writableDatabase.use { db ->
            db.execSQL("DELETE FROM $TABLE_NAME")
        }
    }

    fun getAccessToken(): String {
        val sql = "SELECT $COLUMN_ACCESS_TOKEN FROM $TABLE_NAME"
        readableDatabase.use { db ->
            db.rawQuery(sql, null).use { cursor ->
                return if (cursor.moveToFirst()) cursor.getString(0) else ""
            }
        }
    }

    fun getRefreshToken(): String {
        val sql = "SELECT $COLUMN_REFRESH_TOKEN FROM $TABLE_NAME"
        readableDatabase.use { db ->
            db.rawQuery(sql, null).use { cursor ->
                return if (cursor.moveToFirst()) cursor.getString(0) else ""
            }
        }
    }

    fun getUID(): String {
        val sql = "SELECT $COLUMN_ID FROM $TABLE_NAME"
        readableDatabase.use { db ->
            db.rawQuery(sql, null).use { cursor ->
                return if (cursor.moveToFirst()) cursor.getString(0) else ""
            }
        }
    }

    fun updateAccessToken(newToken: String) {
        val userId = getUID()
        val accessToken = "Bearer $newToken"
        val sql = "UPDATE $TABLE_NAME SET $COLUMN_ACCESS_TOKEN = ? WHERE $COLUMN_ID = ?"
        writableDatabase.use { db ->
            db.execSQL(sql, arrayOf(accessToken, userId))
        }
    }

    fun updateRefreshToken(newToken: String) {
        val userId = getUID()
        val sql = "UPDATE $TABLE_NAME SET $COLUMN_REFRESH_TOKEN = ? WHERE $COLUMN_ID = ?"
        writableDatabase.use { db ->
            db.execSQL(sql, arrayOf(newToken, userId))
        }
    }

    fun saveAuth(user: UserDto, jwt: JwtDto) {
        deleteAllAuth() // 기존 데이터 삭제
        val accessToken = "Bearer ${jwt.accessToken}"
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_SEQ, user.seq)
                put(COLUMN_ID, user.userId)
                put(COLUMN_NAME, user.userName)
                put(COLUMN_ROLE, user.role)
                put(COLUMN_EMAIL, user.email)
                put(COLUMN_ACCESS_TOKEN, accessToken)
                put(COLUMN_REFRESH_TOKEN, jwt.refreshToken)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    fun getUserInfo(): UserDto? {
        val sql = """
            SELECT $COLUMN_SEQ, $COLUMN_ID, $COLUMN_NAME, $COLUMN_ROLE, $COLUMN_EMAIL 
            FROM $TABLE_NAME
        """
        readableDatabase.use { db ->
            db.rawQuery(sql, null).use { cursor ->
                return if (cursor.moveToFirst()) {
                    UserDto(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4), // 이메일
                        cursor.getString(3)  // 역할
                    )
                } else null
            }
        }
    }

    fun getIntRole(): Int {
        val stringRole = getUserInfo()?.role ?: return 0
        return when (stringRole) {
            "ROLE_USER" -> 1
            "ROLE_MANAGER" -> 2
            "ROLE_SENIOR_MANAGER" -> 3
            else -> 0
        }
    }
}


