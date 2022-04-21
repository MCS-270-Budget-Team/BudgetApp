package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException
import java.text.SimpleDateFormat

class ExpenseViewer : AppCompatActivity() {
    private lateinit var experienceBar: ProgressBar
    private lateinit var earningBar: ProgressBar
    private lateinit var spendingBar: ProgressBar

    private lateinit var expenseListview : ListView
    private var expenseViewAdapter: ExpenseViewAdapter? = null
    private lateinit var addButton: Button
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var homepageButton: ImageButton
    private lateinit var totalAmount: TextView
    private lateinit var upcomingBillButton: ImageButton

    private lateinit var avatar: ImageView
    private lateinit var levelText: TextView

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
        experienceBar = findViewById(R.id.experienceBar)
        earningBar = findViewById(R.id.earningBar)
        spendingBar = findViewById(R.id.spendingBar)

        avatar = findViewById(R.id.avatar)

        expenseListview = findViewById(R.id.expense_listview)
        addButton = findViewById(R.id.add_bill_btn)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        homepageButton = findViewById(R.id.add_entry_button)
        totalAmount = findViewById(R.id.total_amount)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)
        levelText = findViewById(R.id.level)

        //get the level
        levelText.text = "Level ${db.getLevel()}"

        //calculate the total amount of money left
        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()
        totalAmount.text = "Total Amount: $$totalMoney"
        spendingBar.progress = (db.addExpenseAmount() / db.addPaycheckAmount() * 100).toInt()

        //set up the bars
        spendingBar.progress = (db.addExpenseAmount() / db.addPaycheckAmount() * 100).toInt()
        experienceBar.progress = ((db.getExp() - db.get_level_exp(db.getLevel())).toDouble() / (db.get_levelup_exp()) * 100).toInt()
        earningBar.progress = (totalMoney / db.getEarning() * 100).toInt()

        //set up the avatar
        val drawableId = this.resources.getIdentifier(db.getAvatar(), "drawable", context.packageName)
        avatar.setImageResource(drawableId)

        //Access the expense and paycheck databases
        val entryDB = EntriesDB(applicationContext)
        expenseViewAdapter = ExpenseViewAdapter(this, entryDB.readData())
        expenseListview.adapter = expenseViewAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddEntries::class.java)
            startActivity(intent)
        }

        adjustExpenseButton.setOnClickListener {
            val intent = Intent(this, AdjustExpense::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        homepageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }
        upcomingBillButton.setOnClickListener {
            val intent = Intent(this, RecurringViewer::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

    }

}

/*
* A PopUp Window for User to Input their entries (could be paycheck or expense).
* Included all the flags, including numeric type and non-empty flag types.
*/
class AddEntries: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var title: EditText
    private lateinit var categories: Spinner
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    private var selectedCategories: String = ""
    private val categoriesOption = arrayOf("paycheck", "expense")

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entries)

        categories = findViewById(R.id.categories)
        amount = findViewById(R.id.amount)
        title = findViewById(R.id.title)
        date = findViewById(R.id.date)
        addButton = findViewById(R.id.add)
        cancelButton = findViewById(R.id.cancel)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesOption)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categories.adapter = adapter
        categories.onItemSelectedListener = context


        addButton.setOnClickListener {
            if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (!isValidDate(date.text.toString())){
                val toast = Toast.makeText(this, "Date format is invalid. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString() == ""){
                val toast = Toast.makeText(this, "Title cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(selectedCategories == ""){
                val toast = Toast.makeText(this, "Categories cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString().length > 10){
                val toast = Toast.makeText(this, "Title cannot exceed 10 characters. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newEntry = Entry(
                    null,
                    title.text.toString(),
                    date.text.toString(),
                    amount.text.toString().toDouble(),
                    selectedCategories
                )
                db.insertData(newEntry)
                val intent = Intent(this, ExpenseViewer::class.java)
                startActivity(intent)
            }
        }

        // if the user does not want to add anything, let them return to the homepage
        cancelButton.setOnClickListener {
            val intent = Intent(this, ExpenseViewer::class.java)
            startActivity(intent)
        }

    }
    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        selectedCategories = categoriesOption[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        val toast = Toast.makeText(this, "Must select a categories!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
        toast.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(inDate: String): Boolean {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            return false
        }
        return true
    }
}

/* A PopUp window that allow the user to edit their paycheck/expense inputs.
* Included all flag types, including the non-numeric and non-empty flags.
* Preserve the user old input as text (not hint) when the popup window show up. */
class EditEntries: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var title: EditText
    private lateinit var categories: Spinner
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private var categoriesOption = arrayOf("paycheck", "expense")
    private var selectedCategories = ""

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entries)

        categories = findViewById(R.id.categories)
        amount = findViewById(R.id.amount)
        title = findViewById(R.id.title)
        date = findViewById(R.id.date)
        addButton = findViewById(R.id.edit)
        cancelButton = findViewById(R.id.cancel)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesOption)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categories.adapter = adapter
        categories.onItemSelectedListener = this

        val extras = intent.extras

        val id = extras!!.getInt("id")

        val titleHint = extras.getString("title")
        val dateHint = extras.getString("date")
        val amountHint = extras.getString("amount")
        //The key argument here must match that used in the other activity

        date.setText(dateHint)
        amount.setText(amountHint)
        title.setText(titleHint)

        addButton.setOnClickListener {
            if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (!isValidDate(date.text.toString())){
                val toast = Toast.makeText(this, "Date format is invalid. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString() == ""){
                val toast = Toast.makeText(this, "Title cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString().length > 10){
                val toast = Toast.makeText(this, "Title cannot exceed 10 characters. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(selectedCategories == ""){
                val toast = Toast.makeText(this, "Categories cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newEntry = Entry(
                    null,
                    title.text.toString(),
                    date.text.toString(),
                    amount.text.toString().toDouble(),
                    selectedCategories
                )
                db.updateData(id, newEntry)
                val intent = Intent(this, ExpenseViewer::class.java)
//            intent.putExtra("newExpense", newExpense)
                startActivity(intent)
            }
        }

        //if the user don't want to change anything, let them return to the homepage
        cancelButton.setOnClickListener {
            val intent = Intent(this, ExpenseViewer::class.java)
            startActivity(intent)
        }

    }
    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedCategories = categoriesOption[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        val toast = Toast.makeText(this, "Must select a categories!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
        toast.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(inDate: String): Boolean {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            return false
        }
        return true
    }

}

