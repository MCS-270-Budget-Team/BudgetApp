package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "BUDGET APP DATABASE"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "PaycheckTable"
private const val ORIGIN_COL = "Origin"
private const val DATE_COL = "date"
private const val AMOUNT_COL = "amount"
private const val ID_COL = "id"

class PaycheckDB(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                ORIGIN_COL + " TEXT," +
                DATE_COL + " DATE," +
                AMOUNT_COL + " INTEGER" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(expense: Expense): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ORIGIN_COL, expense.title)
        contentValues.put(DATE_COL, expense.date)
        contentValues.put(AMOUNT_COL, expense.amount)
        val result = database.insert(TABLE_NAME, null, contentValues)
        return result
    }

    @SuppressLint("Range")
    fun readData(): MutableList<Expense> {
        val list: MutableList<Expense> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val expense = Expense()
                expense.title = result.getString(result.getColumnIndex(ORIGIN_COL))
                expense.date = result.getString(result.getColumnIndex(DATE_COL))
                expense.amount = result.getString(result.getColumnIndex(AMOUNT_COL)).toDouble()
                list.add(expense)
            }
            while (result.moveToNext())
        }
        return list
    }

}