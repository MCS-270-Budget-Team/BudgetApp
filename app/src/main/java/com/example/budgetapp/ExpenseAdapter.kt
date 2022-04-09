package com.example.budgetapp

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class ExpenseAdapter(var context: Context, var arraylist: MutableList<Expense>): BaseAdapter() {
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
        val view: View = View.inflate(context, R.layout.expense_item, null)
        var categories: TextView = view.findViewById(R.id.expense_categories)
        var percent: TextView = view.findViewById(R.id.expense_percent)
        var max: TextView = view.findViewById(R.id.expense_max)
        var action: Button = view.findViewById(R.id.action)

        var expense: Expense = arraylist[p0]

        categories.setText(expense.categories)
        percent.setText(expense.percentage.toString() + "%")
        max.setText("$" + expense.max.toString())

        action.setOnClickListener {
            // start new activity
            // calc budget
            remove(p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Delete category successfully!", Toast.LENGTH_SHORT).show()
        }

        return view!!
    }

    fun remove(position: Int) {
        arraylist.remove(arraylist.get(position))
    }

}

