package com.example.budgetapp

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseViewer : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)
        tableLayout = findViewById(R.id.expense_table)
        val context = this
        //Access the expense database, this will also need to access the paycheck database in future
        val db = ExpenseDB(context)
        //Get the list of all expenses from the database.
        //val expenseList = db.readData()
        val e1 = Expense()
        val e2 = Expense(amount = "$12")
        val e3 = Expense(date = "2/22/22")
        var expenseList = mutableListOf(e1, e2, e3)

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
            amount.text = expenseList[i].amount

            tr.addView(name)
            tr.addView(date)
            tr.addView(category)
            tr.addView(amount)

            //Add this row to the expense viewing table
            tableLayout.addView(tr)
        }


    }

}