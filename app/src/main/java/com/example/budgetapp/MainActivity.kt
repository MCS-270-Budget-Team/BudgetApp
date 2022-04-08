package com.example.budgetapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addEntryButton: Button
    private lateinit var adjustExpenseButton: Button
    private lateinit var viewHistoryButton: Button
    private lateinit var upcomingBillButton: Button
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addEntryButton = findViewById(R.id.add_entry_button)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)


        addEntryButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, AddEntries::class.java) //
            startActivity(intent)
        }

        adjustExpenseButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, AdjustExpense::class.java) //
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            startActivity(intent)
        }

        upcomingBillButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, BillsViewer::class.java) //
            startActivity(intent)
        }
    }

}
