package com.example.budgetapp

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.w3c.dom.Text

class RecurringAdapter(var context: Context, var arraylist: MutableList<RecurringExpense>): BaseAdapter() {
    private val entryDB = EntriesDB(context)
    override fun getCount(): Int {
        return arraylist.size
    }

    override fun getItem(p0: Int): Any {
        return arraylist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.recurring_expense_item, null)
        var name: TextView = view.findViewById(R.id.recurring_name)
        var amount: TextView = view.findViewById(R.id.recurring_amount)
        var deleteButton: ImageButton = view.findViewById(R.id.recurring_action)
        var nextDateToPayOn: TextView = view.findViewById(R.id.recurring_date)

        var expense: RecurringExpense = arraylist[p0]

        name.setText(expense.title)
        amount.setText(expense.amount.toString())
        nextDateToPayOn.setText(expense.date)

        deleteButton.setOnClickListener {
            // start new activity
            // calc budget
            remove(expense.id!!, p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Successfully deleted recurring expense!", Toast.LENGTH_SHORT).show()
        }

        return view!!
    }

    private fun remove(id: Int, position: Int) {
        entryDB.deleteRow_Recurring(id)
        arraylist.remove(arraylist.get(position))
    }

}

