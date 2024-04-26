package com.willow.todolist.Datas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.willow.todolist.Models.Mission

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    companion object {
        private const val DATABASE_NAME = "todoList.db"
        private const val TABLE_NAME = "mission"
        private const val ID = "ID"
        private const val CONTENT = "CONTENT"
        private const val TIME = "TIME"
        private const val DATE = "DATE"
        private const val STATUS = "STATUS"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $CONTENT TEXT, $TIME TEXT, $DATE TEXT, $STATUS INTEGER )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(mission: Mission) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(CONTENT, mission.content)
            put(TIME, mission.time)
            put(DATE, mission.date)
            put(STATUS, 0)
        }
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllMission(): List<Mission> {
        val missionList = mutableListOf<Mission>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(TIME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(STATUS))
            val mission = Mission(id, content, time, date, status)
            missionList.add(mission)
        }
        cursor.close()
        db.close()
        return missionList
    }

    fun updateMission(mission: Mission) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(CONTENT, mission.content)
            put(TIME, mission.time)
            put(DATE, mission.date)
        }
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(mission.id.toString())
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
        db.close()
    }

    fun getById(missionId: Int): Mission {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID = $missionId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(TIME))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
        val status = cursor.getInt(cursor.getColumnIndexOrThrow(STATUS))
        cursor.close()
        db.close()
        return Mission(id, content, time, date, status)
    }

    fun deleteMission(missionId: Int) {
        val db = writableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(missionId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun updateStatus(missionId: Int, status: Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(missionId.toString())
        contentValues.put(STATUS, status)
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
    }

    fun getByDate(date: String): List<Mission> {
        val missionList = mutableListOf<Mission>()
        val db = readableDatabase
        val query = "SELECT * FROM mission WHERE DATE = \"$date\""
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(TIME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(STATUS))
            val mission = Mission(id, content, time, date, status)
            missionList.add(mission)
        }
        cursor.close()
        return missionList
    }

}
