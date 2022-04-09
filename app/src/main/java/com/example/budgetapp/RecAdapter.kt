package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.min

class RecAdapter(var context: Context, var arraylist: MutableList<Expense>): BaseAdapter() {
    private var totalAmount: Double = 1000.0

    override fun getCount(): Int {
        return arraylist.size
    }

    override fun getItem(p0: Int): Any {
        return arraylist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.rec_expense_item, null)

        var categories: TextView = view.findViewById(R.id.rec_categories)
        var amount: TextView = view.findViewById(R.id.rec_amount)
        var percent: TextView = view.findViewById(R.id.rec_percent)

        var expense: Expense = arraylist[p0]
        categories.text = expense.categories

        val ideal_percent = expense.percentage

        amount.text = min(totalAmount * ideal_percent / 100, expense.max.toDouble()).toString()
        percent.text = ((amount.text as String).toDouble()/totalAmount * 100).toInt().toString()

        amount.text = "$" + amount.text
        percent.text = percent.text as String + "%"

        return view!!
    }

}

