package com.example.budgetapp

import android.os.Bundle
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseViewer : AppCompatActivity() {
//    private lateinit var currentAmount: TextView
    private lateinit var expenseListview : ListView
    private var expenseViewAdapter: ExpenseViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
//        currentAmount = findViewById(R.id.current_amount)
        expenseListview = findViewById(R.id.expense_listview)
        val context = this
        //Access the expense and paycheck databases
        val entryDB = EntriesDB(applicationContext)

        expenseViewAdapter = ExpenseViewAdapter(applicationContext, entryDB.readData())
        expenseListview?.adapter = expenseViewAdapter

    }

}