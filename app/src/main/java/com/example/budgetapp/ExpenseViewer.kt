package com.example.budgetapp

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseViewer : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var accountAmount : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
        tableLayout = findViewById(R.id.expense_table)
        accountAmount = findViewById(R.id.account_amount)
        val context = this
        //Access the expense and paycheck databases
        val expenses = ExpenseDB(context)
        val paychecks = PaycheckDB(context)
        //Get the list of all expenses and paychecks from the databases.
        val expenseList = expenses.readData()
        val paycheckList = paychecks.readData()

        for(i in 0 until paycheckList.size) {
            var tr = TableRow(context)
            tr.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            var origin = TextView(context)
            origin.text = paycheckList[i].origin
            var date = TextView(context)
            date.text = paycheckList[i].date
            var amount = TextView(context)
            amount.text = paycheckList[i].amount.toString()
            var category = TextView(context)
            category.text = "Paycheck"

            tr.addView(origin)
            tr.addView(date)
            tr.addView(category)
            tr.addView(amount)

            tableLayout.addView(tr)
        }

        //Need to add each expense as a new TableRow into the TableLayout contained in activity_expense_view
        for(i in 0 until expenseList.size) {
            //Create a TableRow containing all of the expense information, and then add it to the TableLayout
            var tr = TableRow(context)
            tr.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            //Need to create the text views for the columns
            var name = TextView(context)
            name.text = expenseList[i].title
            var date = TextView(context)
            date.text = expenseList[i].date
            var category = TextView(context)
            category.text = expenseList[i].categories
            var amount = TextView(context)
            amount.text = expenseList[i].amount.toString()

            tr.addView(name)
            tr.addView(date)
            tr.addView(category)
            tr.addView(amount)

            //Add this row to the expense viewing table
            tableLayout.addView(tr)
        }

        //Compute the result of all the expenses + all the paychecks
        val currentSum = expenses.addAllAmount() + paychecks.addAllAmount()
        //Update the textview with the correct amount of money
        accountAmount.text = "$$currentSum"

    }

}