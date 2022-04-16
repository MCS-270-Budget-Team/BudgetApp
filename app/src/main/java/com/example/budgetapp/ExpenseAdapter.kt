package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.math.min

class ExpenseAdapter(var context: Context, private var arraylist: MutableList<Expense>): BaseAdapter() {
    override fun getCount(): Int {
        return arraylist.size
    }

    override fun getItem(p0: Int): Any {
        return arraylist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.expense_item, null)
        val categories: TextView = view.findViewById(R.id.expense_categories)
        val percent: TextView = view.findViewById(R.id.expense_percent)
        val max: TextView = view.findViewById(R.id.expense_max)
        val rec: TextView = view.findViewById(R.id.expense_rec)
        val action: ImageButton = view.findViewById(R.id.action)

        val expense: Expense = arraylist[p0]

        categories.text = expense.categories
        percent.text = expense.percentage.toInt().toString() + "%"
        max.text = "Max: $" + expense.max.toInt().toString()

        rec.text = "Rec: $" + min(expense.percentage/100 * 1000.0, expense.max).toString()

        action.setOnClickListener {
            // start new activity
            // calc budget
            remove(p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Delete category successfully!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun remove(position: Int) {
        arraylist.remove(arraylist[position])
    }

}

