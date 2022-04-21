package com.example.budgetapp

import android.annotation.SuppressLint
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

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.recurring_expense_item, null)
        val name: TextView = view.findViewById(R.id.recurring_name)
        val amount: TextView = view.findViewById(R.id.recurring_amount)
        val category: TextView = view.findViewById(R.id.recurring_category)
        val deleteButton: ImageButton = view.findViewById(R.id.recurring_action)
        val nextDateToPayOn: TextView = view.findViewById(R.id.recurring_date)

        val expense: RecurringExpense = arraylist[p0]

        name.text = expense.title
        amount.text = expense.amount.toString()
        nextDateToPayOn.text = expense.date
        category.text = expense.categories

        deleteButton.setOnClickListener {
            // start new activity
            // calc budget
            remove(expense.id!!, p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Successfully deleted recurring expense!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun remove(id: Int, position: Int) {
        entryDB.deleteRow_Recurring(id)
        arraylist.remove(arraylist.get(position))
    }

}

