/*
Tutorial link: https://www.tutorialspoint.com/how-to-use-a-simple-sqlite-database-in-kotlin-android

Creating the ExpenseDB object
    val context = this
    val db = ExpenseDB(context)

To get the list of existing expense, use the function readData(), which takes nothing and
return a list of Expense objects:
    val existing_expenses = db.readData()

To insert a new expense input into the database, use the function insertData(), which takes
an newly created Expense object and return the id of the new expenses:
    val expense = Expense() #insert your own value
    db.insertData(expense)

To delete a specific entries in the database, we need to specify the id of the entries
that we want to delete. The following code will delete the entries with id = 1
    dbHelper.deleteData(1)

To delete all entries in the table (not recommended, only for debugging purposes), call the
function below
    dbHelper.deleteAllData()

To update an entries in the table, we need to specify the id of the entries and the new object
we want to replace the old object with. The code below replace the entries with id = 1 with
the new expense object
    val new_expense = Expense(null, "cafe", "04/05/2022", 20.0, "others")
    dbHelper.updateData(id, new_expense)
*/

package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "BUDGET APP DATABASE"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "ExpenseTable"
private const val TITLE_COL = "title"
private const val DATE_COL = "date"
private const val AMOUNT_COL = "amount"
private const val CATEGORIES_COL = "categories"
private const val ID_COL = "id"

class ExpenseDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT," +
                DATE_COL + " DATE," +
                AMOUNT_COL + " REAL," +
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

    fun insertData(expense: Expense): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE_COL, expense.title)
        contentValues.put(DATE_COL, expense.date)
        contentValues.put(AMOUNT_COL, expense.amount)
        contentValues.put(CATEGORIES_COL, expense.categories)
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
                expense.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                expense.title = result.getString(result.getColumnIndex(TITLE_COL))
                expense.date = result.getString(result.getColumnIndex(DATE_COL))
                expense.amount = result.getString(result.getColumnIndex(AMOUNT_COL)).toDouble()
                expense.categories = result.getString(result.getColumnIndex(CATEGORIES_COL))
                list.add(expense)
            }
            while (result.moveToNext())
        }
        return list
    }

    fun getLength(): Int{
        val all_entries = this.readData()
        return all_entries.size
    }

    fun updateData(id: Int?, new_expense: Expense){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME SET $TITLE_COL = \'${new_expense.title}\', " +
                    "$DATE_COL = \'${new_expense.date}\', " +
                    "${AMOUNT_COL} = ${new_expense.amount}, " +
                    "${CATEGORIES_COL} = \'${new_expense.categories}\' " +
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

}
