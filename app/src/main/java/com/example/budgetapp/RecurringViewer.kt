package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException
import java.text.SimpleDateFormat


class RecurringViewer : AppCompatActivity() {
    private lateinit var recurringListview : ListView
    private var recurringAdapter: RecurringAdapter? = null
    private lateinit var addButton: Button
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var homepageButton: ImageButton
    private lateinit var totalAmount: TextView
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var upcomingBillButton: ImageButton

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recurring_view)
        recurringListview = findViewById(R.id.recurring_listview)
        addButton = findViewById(R.id.add_recurring_btn)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        homepageButton = findViewById(R.id.add_entry_button)
        totalAmount = findViewById(R.id.total_amount)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)
        viewHistoryButton = findViewById(R.id.view_history_button)

        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()

        totalAmount.text = "Total Amount: $$totalMoney"

        //Access the expense and paycheck databases
        val entryDB = EntriesDB(applicationContext)

        recurringAdapter = RecurringAdapter(applicationContext, entryDB.getAll_Recurring())
        recurringListview.adapter = recurringAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddRecurringBill::class.java)
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

    }

class AddRecurringBill: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var title: EditText
    private lateinit var frequency: Spinner
    private lateinit var categories: Spinner
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    private var selectedCategories: String = ""
    private var selectedFrequency: String = ""
    private val categoriesOption = arrayOf("paycheck", "expense") //This should draw from user-defined categories
    private val frequencyOptions = arrayOf("Weekly", "Monthly", "Annually")

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recurring)

        categories = findViewById(R.id.categories)
        frequency = findViewById(R.id.frequency_spinner)
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

        val frequencyAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frequency.adapter = frequencyAdapter
        frequency.onItemSelectedListener = context


        addButton.setOnClickListener {
            // Should probably prevent the user from creating recurring bills due in the past

            if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (isValidDate(date.text.toString())){
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
                if(selectedFrequency == "") selectedFrequency = "Monthly" //Default to monthly if user doesn't specify

                val newEntry = RecurringExpense(
                    null,
                    title.text.toString(),
                    amount.text.toString().toDouble(),
                    date.text.toString(),
                    selectedCategories,
                    "Placeholder",
                    selectedFrequency,
                )
                db.insert_Recurring(newEntry)
                val intent = Intent(this, RecurringViewer::class.java)
                startActivity(intent)
            }
        }

        // if the user does not want to add anything, let them return to the homepage
        cancelButton.setOnClickListener {
            val intent = Intent(this, RecurringViewer::class.java)
            startActivity(intent)
        }

    }
    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    @SuppressLint("ResourceType")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        //Update the correct variable depending on the id of the spinner that was selected
        when(p0!!.id) {
            R.id.categories -> selectedCategories = categoriesOption[position]
            R.id.frequency_spinner -> selectedFrequency = frequencyOptions[position]
            else -> {}
        }
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

}