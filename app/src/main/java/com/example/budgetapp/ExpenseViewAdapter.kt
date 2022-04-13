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
import android.graphics.Color

class ExpenseViewAdapter(var context: Context, var arraylist: MutableList<Entry>): BaseAdapter() {
    val entryDB = EntriesDB(context)

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
        print(arraylist)

        val view: View = View.inflate(context, R.layout.expense_bubble_activity, null)
        val categories: TextView = view.findViewById(R.id.categories)
        val date: TextView = view.findViewById(R.id.date)
        val amount: TextView = view.findViewById(R.id.amount)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        var editButton: Button = view.findViewById(R.id.edit_button)

        val expense: Entry = arraylist[p0]

        categories.setText(expense.title)
        date.setText(expense.date)
        amount.setText("$${expense.amount.toInt().toString()}")

        if (expense.categories == "paycheck"){
            amount.setTextColor(Color.parseColor("#4CAF50"))
        }
        else{
            amount.setTextColor(Color.parseColor("#ED0606"))
        }

        deleteButton.setOnClickListener {
            // start new activity
            // calc budget
            remove(expense.id!!, p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Delete expense successfully!", Toast.LENGTH_SHORT).show()
        }

        return view!!
    }

    fun remove(id: Int, position: Int) {
        entryDB.deleteData(id)
        arraylist.remove(arraylist.get(position))
    }

}

