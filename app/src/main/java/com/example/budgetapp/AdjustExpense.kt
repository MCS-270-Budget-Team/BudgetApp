package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AdjustExpense: AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var expenseList: ListView
    private lateinit var homepageButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var totalAmount: TextView

    private var expenseAdapter: ExpenseAdapter? = null
    private lateinit var expenseBank: MutableList<Expense>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_expense)

        expenseList = findViewById(R.id.listview)
        addButton = findViewById(R.id.add_categories_btn)
        homepageButton = findViewById(R.id.add_entry_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        totalAmount = findViewById(R.id.total_amount)

        expenseBank = mutableListOf(
            Expense(null, "housing", 30.0, 2000.0),
            Expense(null, "food", 10.0, 500.0),
            Expense(null, "utilities", 5.0, 100.0),
            Expense(null, "transportation", 10.0, 500.0),
            Expense(null, "others", 15.0, 1000.0),
            Expense(null, "savings", 30.0, 3000.0),
        )
        // create an adapter to inflate list view, pass the expense bank to the adapter
        expenseAdapter = ExpenseAdapter(applicationContext, expenseBank)
        expenseList?.adapter = expenseAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddCategories::class.java)
            startActivity(intent)
        }

        homepageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            val intent = Intent(this, ExpenseViewer::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

    }
    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

}

