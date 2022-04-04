/*
Tutorial link: https://www.tutorialspoint.com/how-to-use-a-simple-sqlite-database-in-kotlin-android
*/

package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "BUDGET APP DATABASE"
const val DATABASE_VERSION = 1
const val TABLE_NAME = "ExpenseTable"
const val TITLE_COL = "name"
const val DATE_COL = "age"
const val AMOUNT_COL = "id"
const val CATEGORIES_COL = "categories"
const val ID_COL = "id"

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT," +
                DATE_COL + " TEXT," +
                AMOUNT_COL + " INTEGER," +
                CATEGORIES_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(expense: Expense) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_COL, expense.id)
        contentValues.put(TITLE_COL, expense.title)
        contentValues.put(DATE_COL, expense.date)
        contentValues.put(AMOUNT_COL, expense.amount)
        contentValues.put(CATEGORIES_COL, expense.categories)
        val result = database.insert(TABLE_NAME, null, contentValues)

    }

    @SuppressLint("Range")
    fun readData(): MutableList<Expense> {
        val list: MutableList<Expense> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val expense = Expense()
                expense.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                expense.title = result.getString(result.getColumnIndex(TITLE_COL))
                expense.date = result.getString(result.getColumnIndex(DATE_COL))
                expense.amount = result.getString(result.getColumnIndex(AMOUNT_COL))
                expense.categories = result.getString(result.getColumnIndex(CATEGORIES_COL))
                list.add(expense)
            }
            while (result.moveToNext())
        }
        return list
    }

}
