package com.example.budgetapp

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addEntryButton: ImageButton
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var upcomingBillButton: ImageButton

    private lateinit var experienceBar: ProgressBar
    private lateinit var earningBar: ProgressBar
    private lateinit var spendingBar: ProgressBar

    private lateinit var totalAmount: TextView

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addEntryButton = findViewById(R.id.add_entry_button)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)

        experienceBar = findViewById(R.id.experienceBar)
        earningBar = findViewById(R.id.earningBar)
        spendingBar = findViewById(R.id.spendingBar)

        totalAmount = findViewById(R.id.total_amount)

        adjustExpenseButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, AdjustExpense::class.java) //
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

    }

}
