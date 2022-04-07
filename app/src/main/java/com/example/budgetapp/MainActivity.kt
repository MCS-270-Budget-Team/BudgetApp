package com.example.budgetapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    private lateinit var paycheckAddButton: Button
    private lateinit var billAddButton: Button
    private lateinit var viewExpense: Button
    private lateinit var paycheckAmount: EditText
    private lateinit var billAmount: EditText
    private lateinit var jobInput: EditText
    private lateinit var billTitle: EditText
    private lateinit var dateBill: EditText
    private lateinit var datePaycheck: EditText

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

        val context = this
        val db = ExpenseDB(context)


        paycheckAddButton.setOnClickListener {
            // save in database for amount and message
            if (paycheckAmount.text != null && jobInput != null && datePaycheck != null) {
                val amount = paycheckAmount.text.toString()
                val message = jobInput.text.toString()
                val date = datePaycheck.text.toString()
                val newExpense = Expense(message, date, amount)
                db.insertData(newExpense)
            }
        }
        billAddButton.setOnClickListener {
            // save in database for amount, date, and bill title
        }

        viewExpense.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            startActivity(intent)
        }
    }
}