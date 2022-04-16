package com.example.budgetapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class AddCategories : AppCompatActivity() {
    private lateinit var categories: EditText
    private lateinit var amount: EditText
    private lateinit var percentage: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private var darkStatusBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_categories)

        categories = findViewById(R.id.categories)
        amount = findViewById(R.id.max_amount)
        percentage = findViewById(R.id.percentage)
        addButton = findViewById(R.id.add)
        cancelButton = findViewById(R.id.cancel)

        addButton.setOnClickListener {
            //val newExpense = Expense(null, categories.text.toString(), percentage.text.toString().toDouble(), amount.text.toString().toDouble())
            val intent = Intent(this, AdjustExpense::class.java)
//            intent.putExtra("newExpense", newExpense)
            startActivity(intent)
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AdjustExpense::class.java)
            startActivity(intent)
        }

    }
}