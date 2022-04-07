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

class PaycheckDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                ORIGIN_COL + " TEXT," +
                DATE_COL + " DATE," +
                AMOUNT_COL + " REAL" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(paycheck: Paycheck): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ORIGIN_COL, paycheck.origin)
        contentValues.put(DATE_COL, paycheck.date)
        contentValues.put(AMOUNT_COL, paycheck.amount)
        val result = database.insert(TABLE_NAME, null, contentValues)
        return result
    }

    @SuppressLint("Range")
    fun readData(): MutableList<Paycheck> {
        val list: MutableList<Paycheck> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $DATE_COL ASC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val paycheck = Paycheck()
                paycheck.origin = result.getString(result.getColumnIndex(ORIGIN_COL))
                paycheck.date = result.getString(result.getColumnIndex(DATE_COL))
                paycheck.amount = result.getString(result.getColumnIndex(AMOUNT_COL)).toDouble()
                list.add(paycheck)
            }
            while (result.moveToNext())
        }
        return list
    }

    fun getLength(): Int{
        val all_entries = this.readData()
        return all_entries.size
    }

    fun updateData(id: Int?, new_paycheck: Paycheck){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME SET $ORIGIN_COL = \'${new_paycheck.origin}\', " +
                    "$DATE_COL = \'${new_paycheck.date}\', " +
                    "${AMOUNT_COL} = ${new_paycheck.amount}, " +
                    "WHERE id = $id"

            db.execSQL(query)
        }
    }

    fun deleteData(id:Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME " +
                "WHERE id = $id"
        db.execSQL(query)
    }

    fun deleteAllData(){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun addAllAmount(): Double {
        val db = this.readableDatabase
        val query = "SELECT SUM($AMOUNT_COL) AS Total FROM $TABLE_NAME "
        val sum = db.rawQuery(query, null)
        if (sum.moveToFirst()) {
            return sum.getDouble(sum.getColumnIndex("Total"))
        }

        return 0.0
    }

}