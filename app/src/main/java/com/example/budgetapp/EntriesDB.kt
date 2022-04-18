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

private const val TABLE_NAME_REC = "RecurringBill"
private const val LAST_PAID_COL = "Last_paid"
private const val IS_PAID_COL = "is_paid"

private const val TABLE_NAME_GOAL = "GoalTable"
private const val GOAL_LEVEL_COL = "level"
private const val GOAL_PLUS_COL = "plus"

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
                MAX_COL + " REAL," +
                "CONSTRAINT cate_unique UNIQUE($CATEGORIES_COL)" + ")")

        val query3 = ("CREATE TABLE " + TABLE_NAME_REC + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT," +
                AMOUNT_COL + " REAL," +
                DATE_COL + " TEXT," +
                CATEGORIES_COL + " TEXT," +
                LAST_PAID_COL + " TEXT," +
                IS_PAID_COL + " varchar" + ")")

        val query4 = ("CREATE TABLE " + TABLE_NAME_GOAL + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT," +
                GOAL_PLUS_COL + " INTEGER," +
                GOAL_LEVEL_COL + " INTEGER" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        db.execSQL(query2)
        db.execSQL(query3)
        db.execSQL(query4)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_DIS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_REC")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_GOAL")
        onCreate(db)
    }

    /************************************************************************************************
     ***********************Functions For Entries Table**********************************************
     ************************************************************************************************/
    fun insertData(entry: Entry): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(TITLE_COL, entry.title)
        contentValues.put(DATE_COL, entry.date)
        contentValues.put(AMOUNT_COL, entry.amount)
        contentValues.put(CATEGORIES_COL, entry.categories)
        return database.insert(TABLE_NAME, null, contentValues)

    }

    @SuppressLint("Range")
    fun readData(): MutableList<Entry> {
        val list: MutableList<Entry> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $DATE_COL DESC"
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


    /************************************************************************************************
     ***********************Functions For Money Distribution Table***********************************
     ************************************************************************************************/
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

    fun insert_Distribute(expense: Expense): Long? {
        val database = this.writableDatabase
        if (this.getAll_Distribute().size == 0) {
            val contentValues = ContentValues()
            contentValues.put(CATEGORIES_COL, "Others")
            contentValues.put(PERCENT_COL, 100.0)
            contentValues.put(MAX_COL, 0.0)

            database.insert(TABLE_NAME_DIS, null, contentValues)
        }

        val contentValues = ContentValues()
        contentValues.put(CATEGORIES_COL, expense.categories)
        contentValues.put(PERCENT_COL, expense.percentage)
        contentValues.put(MAX_COL, expense.max)

        if (this.isUnique(expense.categories)) {
            return null
        }
        val result = database.insert(TABLE_NAME_DIS, null, contentValues)
        this.updatePercent()
        return result
    }

    fun delete_Distribute(id:Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_DIS " +
                    "WHERE id = $id"
        db.execSQL(query)
    }

    fun updateRow_Distribute(id: Int?, new_expense:Expense){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME_DIS SET $CATEGORIES_COL = \'${new_expense.categories}\', " +
                    "$PERCENT_COL = \'${new_expense.percentage}\', " +
                    "$MAX_COL = ${new_expense.max} " +
                    "WHERE id = $id"
            db.execSQL(query)
        }
    }

    @SuppressLint("Range")
    fun getRow_Distribute(id: Int): Expense {
        val expense = Expense()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DIS WHERE id = $id"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                expense.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                expense.categories = result.getString(result.getColumnIndex(CATEGORIES_COL))
                expense.percentage = result.getString(result.getColumnIndex(PERCENT_COL)).toDouble()
                expense.max = result.getString(result.getColumnIndex(MAX_COL)).toDouble()
            }
                while (result.moveToNext())
        }
        return expense
    }

    @SuppressLint("Range")
    fun getAll_Distribute(): MutableList<Expense> {
        val list: MutableList<Expense> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DIS"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val expense = Expense()
                expense.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                expense.categories = result.getString(result.getColumnIndex(CATEGORIES_COL))
                expense.percentage = result.getString(result.getColumnIndex(PERCENT_COL)).toDouble()
                expense.max = result.getString(result.getColumnIndex(MAX_COL)).toDouble()
                list.add(expense)
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

    /* only for testing, not recommend using!!! */
    private fun deleteAll_Distribute(){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_DIS"
        db.execSQL(query)
    }

    fun isUnique(category: String): Boolean{
        val categories = this.getCategories_Distribute().map{ it.lowercase() }
        return (categories.contains(category))
    }
    @SuppressLint("Range")
    private fun isValid(percentage: Double): Boolean {
        val db = this.readableDatabase
        val query = "SELECT $PERCENT_COL FROM $TABLE_NAME_DIS WHERE $CATEGORIES_COL = \"Others\""
        val result = db.rawQuery(query, null)
        var otherPercent = 0.0
        if (result.moveToFirst()){
            otherPercent = result.getDouble(result.getColumnIndex(PERCENT_COL))
        }
        return (percentage > otherPercent)
    }

    @SuppressLint("Range")
    private fun updatePercent(){
        val db = this.writableDatabase
        val query = "SELECT SUM($PERCENT_COL) AS Total FROM $TABLE_NAME_DIS WHERE $CATEGORIES_COL != \"Others\""
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            val total = result.getDouble(result.getColumnIndex("Total"))
            val percentage = 100.0 - total
            val query2 = "UPDATE $TABLE_NAME_DIS SET $PERCENT_COL = $percentage WHERE $CATEGORIES_COL = \"Others\""
            db.execSQL(query2)
        }
    }

    /************************************************************************************************
     ***********************Functions For Recurring Bill Table***************************************
     ************************************************************************************************/

    /*
    * To insert a row
    *       val r = RecurringExpense()
    *       db.insert_Recurring(r)
    *
    * To delete a row, you need id
    *       db.deleteRow_Recurring(id)
    *
    * To delete all rows
    *       db.deleteAll_Recurring()
    *
    * To get all rows
    *       val result: MutableList<RecurringExpense> = db.getAll_Recurring
    *
    * To update a row
    *       db.updateRow_Recurring(id, new_recurring)
    **/

    fun insert_Recurring(recurring: RecurringExpense): Long? {
        val database = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(TITLE_COL, recurring.title)
        contentValues.put(AMOUNT_COL, recurring.amount)
        contentValues.put(DATE_COL, recurring.date)
        contentValues.put(CATEGORIES_COL, recurring.categories)
        contentValues.put(LAST_PAID_COL, recurring.last_paid)
        contentValues.put(IS_PAID_COL, recurring.is_paid.toString())

        return database.insert(TABLE_NAME_REC, null, contentValues)
    }

    fun deleteRow_Recurring(id: Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_REC " +
                "WHERE id = $id"
        db.execSQL(query)
    }

    fun deleteAll_Recurring(){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_REC"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun getAll_Recurring(): MutableList<RecurringExpense> {
        val list: MutableList<RecurringExpense> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_REC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val recurring = RecurringExpense()
                recurring.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                recurring.title = result.getString(result.getColumnIndex(TITLE_COL))
                recurring.amount = result.getString(result.getColumnIndex(AMOUNT_COL)).toDouble()
                recurring.date = result.getString(result.getColumnIndex(DATE_COL))
                recurring.categories = result.getString(result.getColumnIndex(CATEGORIES_COL))
                recurring.last_paid = result.getString(result.getColumnIndex(LAST_PAID_COL))
                recurring.is_paid = result.getString(result.getColumnIndex(IS_PAID_COL)).toBoolean()
                list.add(recurring)
            }
            while (result.moveToNext())
        }
        return list
    }

    fun updateRow_Recurring(id: Int?, new_Recurring: RecurringExpense){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME_REC SET $TITLE_COL = \'${new_Recurring.title}\', " +
                    "$AMOUNT_COL = \'${new_Recurring.amount}\', " +
                    "$DATE_COL = ${new_Recurring.date}, " +
                    "$CATEGORIES_COL = \"${new_Recurring.categories}\", " +
                    "$LAST_PAID_COL = ${new_Recurring.last_paid}, " +
                    "$IS_PAID_COL = \"${new_Recurring.is_paid}\" " +
                    "WHERE id = $id"
            db.execSQL(query)
        }
    }

    /************************************************************************************************
     ***********************Functions For Goal Table*************************************************
     ************************************************************************************************/

    fun insertGoal(goal: Goal): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(TITLE_COL, goal.title)
        contentValues.put(GOAL_LEVEL_COL, goal.level)
        contentValues.put(GOAL_PLUS_COL, goal.plus)

        return database.insert(TABLE_NAME_GOAL, null, contentValues)
    }

    fun deleteGoal(id:Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME_GOAL " +
                "WHERE id = $id"
        db.execSQL(query)
    }

    fun editGoal(id: Int?, newGoal: Goal){
        if (id != null) {
            val db = this.writableDatabase
            val query = "UPDATE $TABLE_NAME_GOAL SET $TITLE_COL = \'${newGoal.title}\', " +
                    "$GOAL_LEVEL_COL = ${newGoal.level}, " +
                    "$GOAL_PLUS_COL = ${newGoal.plus} " +
                    "WHERE id = $id"
            db.execSQL(query)
        }
    }

    @SuppressLint("Range")
    fun getAllGoals(): MutableList<Goal>{
        val list: MutableList<Goal> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_GOAL"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val goal = Goal()
                goal.id = result.getString(result.getColumnIndex(ID_COL)).toInt()
                goal.title = result.getString(result.getColumnIndex(TITLE_COL))
                goal.level = result.getInt(result.getColumnIndex(GOAL_LEVEL_COL))
                goal.plus = result.getInt(result.getColumnIndex(GOAL_PLUS_COL))
                list.add(goal)
            }
            while (result.moveToNext())
        }
        return list
    }

}
