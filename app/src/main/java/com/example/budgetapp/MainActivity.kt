package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = ExpenseDB(this)

        val expense = Expense(null, "groceries", "04/04/2022", 100.5, "others")
        dbHelper.deleteAllData()
        dbHelper.insertData(expense)
        var expenseList = dbHelper.readData()

        val id = expenseList[0].id
        val new_expense = Expense(null, "cafe", "04/05/2022", 20.0, "others")

        dbHelper.updateData(id, new_expense)

        expenseList = dbHelper.readData()
        print(expenseList)
    }
}