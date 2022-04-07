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
        val entryDB = EntriesDB(context)
        //Get the list of all expenses and paychecks from the databases.
        val entryList = entryDB.readData()

        //Need to add each expense as a new TableRow into the TableLayout contained in activity_expense_view
        for(i in 0 until entryList.size) {
            //Create a TableRow containing all of the expense information, and then add it to the TableLayout
            var tr = TableRow(context)
            tr.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            //Need to create the text views for the columns
            var name = TextView(context)
            name.text = entryList[i].title
            var date = TextView(context)
            date.text = entryList[i].date
            var category = TextView(context)
            category.text = entryList[i].categories
            var amount = TextView(context)
            amount.text = entryList[i].amount.toString()

            tr.addView(name)
            tr.addView(date)
            tr.addView(category)
            tr.addView(amount)

            //Add this row to the expense viewing table
            tableLayout.addView(tr)
        }

        //Compute the result of all the expenses + all the paychecks
        val currentSum = entryDB.addPaycheckAmount().toDouble() - entryDB.addExpenseAmount().toDouble()
        //val currentSum = 100.0
        //Update the textview with the correct amount of money
        accountAmount.text = "$$currentSum"

    }

}