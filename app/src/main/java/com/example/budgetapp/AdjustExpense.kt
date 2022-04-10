package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdjustExpense: AppCompatActivity() {

    private lateinit var recommendButton: Button
    private lateinit var addButton: Button
    private lateinit var expenseList: ListView
    private lateinit var addCategory: EditText
    private lateinit var addPercent: EditText
    private lateinit var addMax: EditText

    private var expenseAdapter: ExpenseAdapter? = null
    private lateinit var expenseBank: MutableList<Expense>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adjust_expense)

        recommendButton = findViewById(R.id.recommend_btn)
        expenseList = findViewById(R.id.listview)
        addButton = findViewById(R.id.add_btn)
        addCategory = findViewById(R.id.add_categories)
        addPercent = findViewById(R.id.add_percent)
        addMax = findViewById(R.id.add_max)

        recommendButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@AdjustExpense, RecExpense::class.java) //
            // start new activity without transition animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }
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

        addButton.setOnClickListener{
            if (!isNumeric(addPercent.text.toString())){
                // catch the percent numeric error so the app does not crash
                Toast.makeText(this, "Percentage must be numeric!", Toast.LENGTH_SHORT).show()
            }
            else if (!isNumeric(addMax.text.toString())){
                // catch the max numeric error so the app does not crash
                Toast.makeText(this, "Max must be numeric!", Toast.LENGTH_SHORT).show()
            }
            else {
                expenseBank.add(
                    0, Expense(
                        null,
                        addCategory.text.toString(),
                        addPercent.text.toString().toDouble(),
                        addMax.text.toString().toDouble()
                    )
                )

                expenseAdapter!!.notifyDataSetChanged()
                Toast.makeText(this, "Add new categories successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

}

