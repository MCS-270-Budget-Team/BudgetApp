package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

private lateinit var paycheckAddButton: Button
private lateinit var billAddButton: Button
private lateinit var viewExpense: Button
private lateinit var paycheckAmount: EditText
private lateinit var billAmount: EditText
private lateinit var jobInput: EditText
private lateinit var billTitle: EditText
private lateinit var dateBill: EditText
private lateinit var datePaycheck: EditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paycheckAddButton = findViewById(R.id.add_paycheck_button)
        billAddButton = findViewById(R.id.add_bill_button)
        viewExpense = findViewById(R.id.view_expense_button)
        paycheckAmount = findViewById(R.id.paycheck_amount)
        billAmount = findViewById(R.id.bill_amount)
        jobInput = findViewById(R.id.job_input)
        billTitle = findViewById(R.id.bill_title)
        dateBill = findViewById(R.id.date_input)
        datePaycheck = findViewById(R.id.date1_input)

        paycheckAddButton.setOnClickListener {
            // save in database for amount, date, and job
        }
        billAddButton.setOnClickListener {
            // save in database for amount, date, and bill title
        }
        viewExpense.setOnClickListener {
            // start new activity
            // calc budget
        }
    }
}