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

private const val TABLE_NAME_DIS = "MoneyDistribution"
private const val PERCENT_COL = "percentage"
private const val MAX_COL = "Maximum"

class EntriesDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /*
    * Setup database with three tables
    * */
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

        val query2 = ("CREATE TABLE " + TABLE_NAME_DIS + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                CATEGORIES_COL + " TEXT," +
                PERCENT_COL + " REAL," +
                MAX_COL + " REAL," + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIS)
        onCreate(db)
    }

/************************************Functions For Entry Table************************************/
    fun insertData(entry: Entry): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        
        contentValues.put(TITLE_COL, entry.title)
        contentValues.put(DATE_COL, entry.date)
        contentValues.put(AMOUNT_COL, entry.amount)
        contentValues.put(CATEGORIES_COL, entry.categories)
        val result = database.insert(TABLE_NAME, null, contentValues)
        return result

    }

    @SuppressLint("Range")
    fun readData(): MutableList<Entry> {
        val list: MutableList<Entry> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $DATE_COL ASC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val expense = Entry()
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

    fun updateData(id: Int?, new_expense: Entry){
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

    @SuppressLint("Range")
    fun addPaycheckAmount(): Double {
        val db = this.readableDatabase
        val query = "SELECT SUM($AMOUNT_COL) AS Total FROM $TABLE_NAME WHERE $CATEGORIES_COL = \"paycheck\""
        val sum = db.rawQuery(query, null)
        if (sum.moveToFirst()) {
            return sum.getDouble(sum.getColumnIndex("Total"))
        }

        return 0.0
    }

    @SuppressLint("Range")
    fun addExpenseAmount(): Double {
        val db = this.readableDatabase
        val query = "SELECT SUM($AMOUNT_COL) AS Total FROM $TABLE_NAME WHERE $CATEGORIES_COL != \"paycheck\""
        val sum = db.rawQuery(query, null)
        if (sum.moveToFirst()) {
            return sum.getDouble(sum.getColumnIndex("Total"))
        }

        return 0.0
    }


    /*************************Functions For Money Distribution Table***********************************/
    /*
    * To insert a new data into the Distribution table, you need create a new Distribute object
    * and call insert_Distribute()
    *       val distribute = Distribute()
    *       db.insert_Distribute(Distribute)
    *
    * To delete a row from the Distribute table, you need the id of the row and call delete_Distribute()
    *       db.delete_Distribute(id)
    *
    * To update a row in the Distribute table, you need the id of the row and the new distribute object,
    * then call updateRow_Distribute()
    *       db.updateRow_Distribute(id)
    *
    * To get a row in the Distribute table, you need the id of the row and call getRow_Distribute(),
    * then it returns you a Distribute object
    *       val myDis = db.getRow_Distribute(id)
    *
    * To get all rows in the Distribute table, you need call getAll_Distribute(), then it returns you
    * a MutableList of Distribute objects
    *        val list: MutableList<Distribute> = db.getAll_Distribute()
    *
    * To get a list of categories in the Distribute table, you need call getCategories_Distribute,
    * then it returns you a MutableList of String
    *       val list:MutableList<String> = db.getCategories_Distribute()
    * */


    fun insert_Distribute(distribute: Distribute): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(CATEGORIES_COL, distribute.category)
        contentValues.put(PERCENT_COL, distribute.percentage)
        contentValues.put(MAX_COL, distribute.max_amount)
        val result = database.insert(TABLE_NAME_DIS, null, contentValues)
        return result
    }

    fun delete_Distribute(id:Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_DIS " +
                    "WHERE id = $id"
        db.execSQL(query)
    }

    fun updateRow_Distribute(id: Int?, new_distribute:Distribute){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME_DIS SET $CATEGORIES_COL = \'${new_distribute.category}\', " +
                    "$PERCENT_COL = \'${new_distribute.percentage}\', " +
                    "$MAX_COL = ${new_distribute.max_amount} " +
                    "WHERE id = $id"
            db.execSQL(query)
        }
    }

    @SuppressLint("Range")
    fun getRow_Distribute(id: Int): Distribute {
        val distribute = Distribute()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DIS WHERE id = $id"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                distribute.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                distribute.category = result.getString(result.getColumnIndex(CATEGORIES_COL))
                distribute.percentage = result.getString(result.getColumnIndex(PERCENT_COL)).toDouble()
                distribute.max_amount = result.getString(result.getColumnIndex(MAX_COL)).toDouble()
            }
                while (result.moveToNext())
        }
        return distribute
    }

    @SuppressLint("Range")
    fun getAll_Distribute(): MutableList<Distribute> {
        val list: MutableList<Distribute> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DIS"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val distribute = Distribute()
                distribute.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                distribute.category = result.getString(result.getColumnIndex(CATEGORIES_COL))
                distribute.percentage = result.getString(result.getColumnIndex(PERCENT_COL)).toDouble()
                distribute.max_amount = result.getString(result.getColumnIndex(MAX_COL)).toDouble()
                list.add(distribute)
            }
            while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun getCategories_Distribute(): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT DISTINCT $CATEGORIES_COL FROM $TABLE_NAME_DIS"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val value:String = result.getString(result.getColumnIndex(CATEGORIES_COL))
                list.add(value)
            }
            while (result.moveToNext())
        }
        return list
    }
}
