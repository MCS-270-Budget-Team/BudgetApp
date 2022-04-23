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
import kotlin.math.pow

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
private const val FREQUENCY_COL = "frequency"

private const val TABLE_NAME_GOAL = "GoalTable"
private const val GOAL_LEVEL_COL = "level"
private const val GOAL_PLUS_COL = "plus"

private const val TABLE_NAME_PER = "PersonalInfo"
private const val PER_LEVEL_COL = "level"
private const val PER_EXP_COL = "exp"
private const val PER_AVA_COL = "avatar"
private const val PER_EARN_COL = "earningGoal"

private const val TABLE_NAME_AVATAR = "AvatarInfo"
private const val AVATAR_COL = "avatarTitle"
private const val AVATAR_LEVEL_COL = "level"
private const val IS_CHOSEN_COL = "is_chosen"
private const val IS_ACTIVATED = "is_activated"

class EntriesDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /*
    * Setup database with four tables
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
                FREQUENCY_COL + " TEXT" + ")")

        val query4 = ("CREATE TABLE " + TABLE_NAME_GOAL + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT," +
                GOAL_PLUS_COL + " INTEGER," +
                GOAL_LEVEL_COL + " INTEGER" + ")")

        val query5 = ("CREATE TABLE " + TABLE_NAME_PER + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                PER_LEVEL_COL + " INTEGER," +
                PER_EXP_COL + " INTEGER," +
                PER_AVA_COL + " TEXT," +
                PER_EARN_COL + " REAL" + ")")

        val query6 = "INSERT INTO $TABLE_NAME_PER ($ID_COL, $PER_LEVEL_COL, $PER_EXP_COL, $PER_AVA_COL, $PER_EARN_COL)" +
                    "VALUES (0,0,0,'baby_turtle',3000.0)"

        val query7 = ("CREATE TABLE " + TABLE_NAME_AVATAR + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                AVATAR_COL + " TEXT," +
                AVATAR_LEVEL_COL + " INTEGER," +
                IS_CHOSEN_COL + " TEXT," +
                IS_ACTIVATED + " TEXT" + ")")

        val query8 = "INSERT INTO $TABLE_NAME_AVATAR VALUES " +
                     "(0, 'baby_turtle', 0, 'false', 'false')," +
                    "(1, 'budget_turtle', 3, 'false', 'false')," +
                    "(2, 'happy_turtle', 6, 'false', 'false')," +
                    "(3, 'old_turtle', 9, 'false', 'false')"

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        db.execSQL(query2)
        db.execSQL(query3)
        db.execSQL(query4)
        db.execSQL(query5)
        db.execSQL(query6)
        db.execSQL(query7)
        db.execSQL(query8)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_DIS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_REC")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_GOAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_AVATAR")
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
            contentValues.put(MAX_COL, 10000000000.0)

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
        updatePercent()
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
        updatePercent()
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
    fun isValid(percentage: Double): Boolean {
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
        contentValues.put(FREQUENCY_COL, recurring.frequency)

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
                recurring.frequency = result.getString(result.getColumnIndex(FREQUENCY_COL))
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
                    "$AMOUNT_COL = ${new_Recurring.amount}, " +
                    "$DATE_COL = \'${new_Recurring.date}\', " +
                    "$CATEGORIES_COL = \"${new_Recurring.categories}\", " +
                    "$LAST_PAID_COL = \'${new_Recurring.last_paid}\', " +
                    "$FREQUENCY_COL = \"${new_Recurring.frequency}\" " +
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
    /************************************************************************************************
     ***********************Functions For PersonalInfo Table*****************************************
     ************************************************************************************************/

    fun insertInfo(id: Int, level: Int, exp: Int, avatar: String, earning: Double): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(ID_COL, level)
        contentValues.put(PER_LEVEL_COL, level)
        contentValues.put(PER_EXP_COL, exp)
        contentValues.put(PER_AVA_COL, avatar)
        contentValues.put(PER_EARN_COL, earning)

        return database.insert(TABLE_NAME_PER, null, contentValues)
    }

    fun updateEarning(new_earning: Double){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_PER SET $PER_EARN_COL = ${new_earning} " +
                "WHERE id = 0"
        db.execSQL(query)
    }

    fun updateLevel(new_level: Int){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_PER SET $PER_LEVEL_COL = ${new_level} " +
                "WHERE id = 0"
        db.execSQL(query)
    }

    fun updateExp(new_exp: Int){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_PER SET $PER_EXP_COL = ${new_exp} " +
                "WHERE id = 0"
        db.execSQL(query)
    }

    fun updateAvatar(new_avatar: String){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_PER SET $PER_AVA_COL = \'$new_avatar\' " +
                "WHERE id = 0"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun getEarning(): Double{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PER WHERE id = 0"
        val result = db.rawQuery(query, null)
        var earning = 0.0
        if (result.moveToFirst()){
            do {
                earning = result.getDouble(result.getColumnIndex(PER_EARN_COL))
            }
            while (result.moveToNext())
        }
        return earning
    }

    @SuppressLint("Range")
    fun getLevel(): Int{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PER WHERE id = 0"
        val result = db.rawQuery(query, null)
        var level = 0
        if (result.moveToFirst()){
            do {
                level = result.getInt(result.getColumnIndex(PER_LEVEL_COL))
            }
            while (result.moveToNext())
        }
        return level
    }

    @SuppressLint("Range")
    fun getExp(): Int{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PER WHERE id = 0"
        val result = db.rawQuery(query, null)
        var exp = 0
        if (result.moveToFirst()){
            do {
                exp = result.getInt(result.getColumnIndex(PER_EXP_COL))
            }
            while (result.moveToNext())
        }
        return exp
    }

    @SuppressLint("Range")
    fun getAvatar(): String{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PER WHERE id = 0"
        val result = db.rawQuery(query, null)
        var avatar = ""
        if (result.moveToFirst()){
            do {
                avatar = result.getString(result.getColumnIndex(PER_AVA_COL))
            }
            while (result.moveToNext())
        }
        return avatar
    }

    fun get_level_exp(level: Int): Int{
        // return the number of exp points need to reach this level
        val X = 0.3
        val Y = 2.0

        return (level / X).pow(Y).toInt()
    }

    fun get_levelup_exp(): Int{
        // return the number of experience points needed to reach the next level
        val currentLevel = this.getLevel()
        val nextLevel = currentLevel + 1

        return get_level_exp(nextLevel) - get_level_exp(currentLevel)
    }

    fun updateTable(): Boolean{
        // return whether or not a user reaches a new level
        // return true if the user level up
        // return false otherwise
        var currentLevel = this.getLevel()
        var nextLevelEXP = this.get_level_exp(currentLevel)

        while (nextLevelEXP <= this.getExp()){
            currentLevel += 1
            nextLevelEXP = this.get_level_exp(currentLevel)
        }
        val returnValue = !(currentLevel - 1 == this.getLevel())
        this.updateLevel(currentLevel - 1)
        return returnValue
    }

    /************************************************************************************************
     ***********************Functions For PersonalInfo Table*****************************************
     ************************************************************************************************/

    @SuppressLint("Range")
    fun get_AvatarName(id: Int): String{
        val db = this.readableDatabase
        val query = "SELECT $AVATAR_COL FROM $TABLE_NAME_AVATAR WHERE id = $id"
        val result = db.rawQuery(query, null)
        var name = ""
        if (result.moveToFirst()){
            do {
                name = result.getString(result.getColumnIndex(AVATAR_COL))
            }
            while (result.moveToNext())
        }
        return name
    }

    @SuppressLint("Range")
    fun get_AvatarLevel(id: Int): Int{
        val db = this.readableDatabase
        val query = "SELECT $AVATAR_LEVEL_COL FROM $TABLE_NAME_AVATAR WHERE id = $id"
        val result = db.rawQuery(query, null)
        var level = 0
        if (result.moveToFirst()){
            do {
                level = result.getInt(result.getColumnIndex(AVATAR_LEVEL_COL))
            }
            while (result.moveToNext())
        }
        return level
    }

    @SuppressLint("Range")
    fun get_isChosen(id: Int): Boolean{
        val db = this.readableDatabase
        val query = "SELECT $IS_CHOSEN_COL FROM $TABLE_NAME_AVATAR WHERE id = $id"
        val result = db.rawQuery(query, null)
        var bool = false
        if (result.moveToFirst()){
            do {
                bool = result.getString(result.getColumnIndex(IS_CHOSEN_COL)).toBoolean()
            }
            while (result.moveToNext())
        }
        return bool
    }

    fun update_isChosen(id: Int, new_bool: Boolean){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_AVATAR SET $IS_CHOSEN_COL = \'$new_bool\' " +
                "WHERE id = $id"
        db.execSQL(query)
    }

    fun setDefault_isChosen(){
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_AVATAR SET $IS_CHOSEN_COL = \"false\""
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun get_isActivated(id: Int): Boolean{
        val db = this.readableDatabase
        val query = "SELECT $IS_ACTIVATED FROM $TABLE_NAME_AVATAR WHERE id = $id"
        val result = db.rawQuery(query, null)
        var bool = false
        if (result.moveToFirst()){
            do {
                bool = result.getString(result.getColumnIndex(IS_ACTIVATED)).toBoolean()
            }
            while (result.moveToNext())
        }
        return bool
    }

    fun update_isActivated(){
        val db = this.writableDatabase
        for (i in 0..3){
            if (this.get_AvatarLevel(i) <= this.getLevel() ){
                val query = "UPDATE $TABLE_NAME_AVATAR SET $IS_ACTIVATED = \"true\" " +
                        "WHERE id = $i"
                db.execSQL(query)
            }
            else{
                val query = "UPDATE $TABLE_NAME_AVATAR SET $IS_ACTIVATED = \"false\" " +
                        "WHERE id = $i"
                db.execSQL(query)
            }
        }
    }
}
