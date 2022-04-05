package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = ExpenseDB(this)

        val expense1 = Expense(null, "groceries", "04/04/2022", 100.5, "others")
        val expense2 = Expense(null, "coffee", "04/052022", 30.6, "others")
        val expense3 = Expense(null, "gas", "04/03/2022", 35.2, "others")

        dbHelper.deleteAllData()
        dbHelper.insertData(expense1)
        dbHelper.insertData(expense2)
        dbHelper.insertData(expense3)
        var expenseList = dbHelper.readData()

        var sum_amount = dbHelper.addAllAmount()
//        val id = expenseList[0].id
//        val new_expense = Expense(null, "cafe", "04/05/2022", 20.0, "others")
//
//        dbHelper.updateData(id, new_expense)

//        expenseList = dbHelper.readData()
        print(sum_amount)
        print(expenseList)
    }
}