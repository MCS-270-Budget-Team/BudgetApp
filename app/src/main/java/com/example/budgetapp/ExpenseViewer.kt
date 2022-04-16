package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ExpenseViewer : AppCompatActivity() {
    private lateinit var expenseListview : ListView
    private var expenseViewAdapter: ExpenseViewAdapter? = null
    private lateinit var addButton: Button
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var homepageButton: ImageButton
    private lateinit var totalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
        expenseListview = findViewById(R.id.expense_listview)
        addButton = findViewById(R.id.add_bill_btn)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        homepageButton = findViewById(R.id.add_entry_button)
        totalAmount = findViewById(R.id.total_amount)

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
//            val newEntry = Entry(null, title.text.toString(), date.text.toString(), amount.text.toString().toDouble(), categories.text.toString())
            val intent = Intent(this, ExpenseViewer::class.java)
//            intent.putExtra("newExpense", newExpense)
            startActivity(intent)
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, ExpenseViewer::class.java)
            startActivity(intent)
        }

    }
}