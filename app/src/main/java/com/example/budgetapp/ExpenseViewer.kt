package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ExpenseViewer : AppCompatActivity() {
    private lateinit var expenseListview : ListView
    private var expenseViewAdapter: ExpenseViewAdapter? = null
    private lateinit var addButton: Button
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var homepageButton: ImageButton
    private lateinit var totalAmount: TextView

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
        expenseListview = findViewById(R.id.expense_listview)
        addButton = findViewById(R.id.add_bill_btn)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        homepageButton = findViewById(R.id.add_entry_button)
        totalAmount = findViewById(R.id.total_amount)

        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()

        totalAmount.text = "Total Amount: $$totalMoney"

        //Access the expense and paycheck databases
        val entryDB = EntriesDB(applicationContext)

        expenseViewAdapter = ExpenseViewAdapter(applicationContext, entryDB.readData())
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

    }

}

class AddEntries: AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var categories: EditText
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

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

        addButton.setOnClickListener {
            if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString() == ""){
                val toast = Toast.makeText(this, "Title cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(categories.text.toString() == ""){
                val toast = Toast.makeText(this, "Categories cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (db.isUnique(categories.text.toString())){
                val toast = Toast.makeText(this, "This category has been added. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newEntry = Entry(
                    null,
                    title.text.toString(),
                    date.text.toString(),
                    amount.text.toString().toDouble(),
                    categories.text.toString()
                )
                db.insertData(newEntry)
                val intent = Intent(this, ExpenseViewer::class.java)
//            intent.putExtra("newExpense", newExpense)
                startActivity(intent)
            }
        }
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
}

class EditEntries: AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var categories: EditText
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

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

        val extras = intent.extras

        val id = extras!!.getInt("id")

        val titleHint = extras.getString("title")
        val categoriesHint = extras.getString("categories")
        val dateHint = extras.getString("date")
        val amountHint = extras.getString("amount")
        //The key argument here must match that used in the other activity

        categories.setText(categoriesHint)
        date.setText(dateHint)
        amount.setText(amountHint)
        title.setText(titleHint)

        addButton.setOnClickListener {
            if (!isNumeric(amount.text.toString())){
                val toast = Toast.makeText(this, "Amount must be numeric. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(title.text.toString() == ""){
                val toast = Toast.makeText(this, "Title cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if(categories.text.toString() == ""){
                val toast = Toast.makeText(this, "Categories cannot be empty. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else if (db.isUnique(categories.text.toString())){
                val toast = Toast.makeText(this, "This category has been added. Try again!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            }
            else {
                val newEntry = Entry(
                    null,
                    title.text.toString(),
                    date.text.toString(),
                    amount.text.toString().toDouble(),
                    categories.text.toString()
                )
                db.updateData(id, newEntry)
                val intent = Intent(this, ExpenseViewer::class.java)
//            intent.putExtra("newExpense", newExpense)
                startActivity(intent)
            }
        }
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
}

