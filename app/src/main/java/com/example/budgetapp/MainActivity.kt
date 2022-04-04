package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val dbHelper = ExpenseDB(this)
//
//        val expense = Expense("groceries", "04/04/2022", 100.5, "others")
//        val expenseList = dbHelper.insertData(expense)
//
//        println(expenseList)
    }
}