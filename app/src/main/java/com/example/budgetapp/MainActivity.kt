package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

private lateinit var addButton: Button
private lateinit var viewExpense: Button
private lateinit var amountInput: EditText
private lateinit var messageInput: EditText
private lateinit var dateInput: EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButton = findViewById(R.id.add_button)
        viewExpense = findViewById(R.id.view_expense_button)
        amountInput = findViewById(R.id.amount_input)
        messageInput = findViewById(R.id.message_input)
        dateInput = findViewById(R.id.date_input)

        addButton.setOnClickListener {
            // save in database for amount and message
        }
        viewExpense.setOnClickListener {
            // start new activity
            // calc budget
        }
    }
}