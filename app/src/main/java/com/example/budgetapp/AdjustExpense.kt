package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/**
 * This activity is used with the activity_adjust_expense layout. It allows for users to create
 * different categories, and assign what percentage of their expenses should go to each category.
 */
class AdjustExpense: AppCompatActivity() {
    private lateinit var experienceBar: ProgressBar
    private lateinit var earningBar: ProgressBar
    private lateinit var spendingBar: ProgressBar

    private lateinit var addButton: Button
    private lateinit var expenseList: ListView
    private lateinit var homepageButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var totalAmount: TextView
    private lateinit var upcomingBillButton: ImageButton
    private lateinit var customizeButton: ImageButton

    private var expenseAdapter: ExpenseAdapter? = null
    private lateinit var expenseBank: MutableList<Expense>
    private lateinit var avatar: ImageView
    private lateinit var levelText: TextView

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTheme()
        setContentView(R.layout.activity_adjust_expense)

        expenseList = findViewById(R.id.listview)
        addButton = findViewById(R.id.add_categories_btn)
        homepageButton = findViewById(R.id.add_entry_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        customizeButton = findViewById(R.id.customize_button)
        totalAmount = findViewById(R.id.total_amount)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)
        levelText = findViewById(R.id.level)

        experienceBar = findViewById(R.id.experienceBar)
        earningBar = findViewById(R.id.earningBar)
        spendingBar = findViewById(R.id.spendingBar)

        avatar = findViewById(R.id.avatar)
        //get the level
        levelText.text = "Level ${db.getLevel()}"

        //get the total amount of money
        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()
        totalAmount.text = "Total Amount: $$totalMoney"

        expenseBank = db.getAll_Distribute()
        //set up the progress bars
        spendingBar.progress = (db.addExpenseAmount() / db.addPaycheckAmount() * 100).toInt()
        experienceBar.progress = ((db.getExp() - db.get_level_exp(db.getLevel())).toDouble() / (db.get_levelup_exp()) * 100).toInt()
        earningBar.progress = (totalMoney / db.getEarning() * 100).toInt()

        //set up the avatar
        if (totalMoney >= 0) {
            //if the amount of saving is positive, display the users' chosen avatar
            val drawableId =
                this.resources.getIdentifier(db.getAvatar(), "drawable", context.packageName)
            avatar.setImageResource(drawableId)
        }
        else{
            //else, display the tomb
            val drawableId =
                this.resources.getIdentifier("tomb", "drawable", context.packageName)
            avatar.setImageResource(drawableId)
        }

        // create an adapter to inflate list view, pass the expense bank to the adapter
        expenseAdapter = ExpenseAdapter(this, expenseBank)
        expenseList.adapter = expenseAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddCategories::class.java)
            startActivity(intent)
        }

        homepageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            val intent = Intent(this, ExpenseViewer::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        upcomingBillButton.setOnClickListener {
            val intent = Intent(this, RecurringViewer::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }
        customizeButton.setOnClickListener {
            val intent = Intent(this@AdjustExpense, Avatar::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

    }

    /**
     * Sets the correct theme based on the themeID stored in the database.
     */
    private fun setTheme(){
        when (db.getThemeID()) {
            0 -> {
                setTheme(R.style.Theme_BudgetApp)
            }
            1 -> {
                setTheme(R.style.Forest)
            }
            2 -> {
                setTheme(R.style.Eggplant)
            }
            3 -> {
                setTheme(R.style.Pumpkin)
            }
            else -> {
                setTheme(R.style.Vintage)
            }
        }
    }
}

/**
 * This activity is used with the activity_add_categories layout. It manages the pop-up that
 * appears when adding a new category from the activity_adjust_expense layout.
 */
class AddCategories : AppCompatActivity() {
    private lateinit var categories: EditText
    private lateinit var amount: EditText
    private lateinit var percentage: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTheme()
        setContentView(R.layout.activity_add_categories)

        categories = findViewById(R.id.categories)
        amount = findViewById(R.id.max_amount)
        percentage = findViewById(R.id.percentage)
        addButton = findViewById(R.id.add)
        cancelButton = findViewById(R.id.cancel)

        addButton.setOnClickListener {
            if (!isNumeric(percentage.text.toString())){
                val toast = Toast.makeText(this, "Percentage must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (percentage.text.toString().toDouble() > 100.0 || percentage.text.toString().toDouble() <= 0){
                val toast = Toast.makeText(this, "Percentage must be within the range of 0 and 100. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (categories.text.toString() == ""){
                val toast = Toast.makeText(this, "Category must not be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (db.isUnique(categories.text.toString())){
                val toast = Toast.makeText(this, "This category has been added. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (db.isValid(percentage.text.toString().toDouble())){
                val toast = Toast.makeText(this, "The entered percentage is too high. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (categories.text.toString().length >10 ){
                val toast = Toast.makeText(this, "Categories cannot exceed 10 characters. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newExpense = Expense(
                    null,
                    categories.text.toString(),
                    percentage.text.toString().toDouble(),
                    amount.text.toString().toDouble()
                )
                //Add the new expense category into the database
                db.insert_Distribute(newExpense)
                val intent = Intent(this, AdjustExpense::class.java)
//              intent.putExtra("newExpense", newExpense)
                //Return back to the adjust_expense activity
                startActivity(intent)
            }
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AdjustExpense::class.java)
            startActivity(intent)
        }

    }

    /**
     * This function determines whether a string can be safely converted to a number.
     */
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    /**
     * This function sets the theme according to the themeID stored in the database.
     */
    private fun setTheme(){
        when (db.getThemeID()) {
            0 -> {
                setTheme(R.style.Theme_BudgetApp)
            }
            1 -> {
                setTheme(R.style.Forest)
            }
            2 -> {
                setTheme(R.style.Eggplant)
            }
            3 -> {
                setTheme(R.style.Pumpkin)
            }
            else -> {
                setTheme(R.style.Vintage)
            }
        }
    }
}

/**
 * This activity uses the activity_edit_categories layout, and allows for previously created
 * expense categories to be modified.
 */
class EditCategories : AppCompatActivity() {
    private lateinit var categories: EditText
    private lateinit var amount: EditText
    private lateinit var percentage: EditText
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTheme()
        setContentView(R.layout.activity_edit_categories)

        categories = findViewById(R.id.categories)
        amount = findViewById(R.id.max_amount)
        percentage = findViewById(R.id.percentage)
        editButton = findViewById(R.id.edit)
        cancelButton = findViewById(R.id.cancel)

        val extras = intent.extras

        //Receive the information about the category to be edited from the ExpenseAdapter
        val id = extras!!.getInt("id")
        val categoriesHint = extras.getString("categories")
        val percentageHint = extras.getString("percentage")
        val amountHint = extras.getString("max_amount")

        categories.setText(categoriesHint)
        percentage.setText(percentageHint)
        amount.setText(amountHint)

        editButton.setOnClickListener {
            if (!isNumeric(percentage.text.toString())){
                val toast = Toast.makeText(this, "Percentage must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (percentage.text.toString().toDouble() > 100.0 || percentage.text.toString().toDouble() <= 0){
                val toast = Toast.makeText(this, "Percentage must be within the range of 0 and 100. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (categories.text.toString() == ""){
                val toast = Toast.makeText(this, "Category must not be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (categories.text.toString() != categoriesHint &&
                db.isUnique(categories.text.toString())){
                val toast = Toast.makeText(this, "This category has been added. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (db.isValid(percentage.text.toString().toDouble())){
                val toast = Toast.makeText(this, "The entered percentage is too high. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (categories.text.toString().length >10 ){
                val toast = Toast.makeText(this, "Categories cannot exceed 10 characters. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newExpense = Expense(
                    null,
                    categories.text.toString(),
                    percentage.text.toString().toDouble(),
                    amount.text.toString().toDouble()
                )
                //Update this expense category with the new changes
                db.updateRow_Distribute(id, newExpense)
                val intent = Intent(this, AdjustExpense::class.java)
//            intent.putExtra("newExpense", newExpense)
                //Go back to the AdjustExpense activity
                startActivity(intent)
            }
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AdjustExpense::class.java)
            startActivity(intent)
        }

    }

    /**
     * This function determines whether or not a string can be converted into a number.
     */
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    /**
     * Sets the current theme according to the themeID stored in the database.
     */
    private fun setTheme(){
        when (db.getThemeID()) {
            0 -> {
                setTheme(R.style.Theme_BudgetApp)
            }
            1 -> {
                setTheme(R.style.Forest)
            }
            2 -> {
                setTheme(R.style.Eggplant)
            }
            3 -> {
                setTheme(R.style.Pumpkin)
            }
            else -> {
                setTheme(R.style.Vintage)
            }
        }
    }
}


