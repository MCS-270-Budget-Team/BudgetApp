package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.math.min

class ExpenseAdapter(var context: Context, private var arraylist: MutableList<Expense>): BaseAdapter() {
    private val db = EntriesDB(context)

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
        val deleteButton: ImageButton = view.findViewById(R.id.action)
        val editButton: ImageButton = view.findViewById(R.id.edit)

        val expense: Expense = arraylist[p0]

        categories.text = expense.categories
        percent.text = expense.percentage.toInt().toString() + "%"
        max.text = "Max: $" + expense.max.toInt().toString()

        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()

        rec.text = "Rec: $" + min(expense.percentage/100 * totalMoney, expense.max).toInt().toString()

        if (expense.categories == "Others"){
            deleteButton.isEnabled = false
            editButton.isEnabled = false
            max.text = ""
        }

        deleteButton.setOnClickListener {
            // start new activity
            // calc budget
            remove(arraylist[p0].id!!)
            notifyDataSetChanged()
            Toast.makeText(context, "Delete category successfully!", Toast.LENGTH_SHORT).show()
        }

        editButton.setOnClickListener {
            val intent = Intent(context, EditCategories::class.java)
//            intent.putExtra("newExpense", newExpense
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", arraylist[p0].id)
            intent.putExtra("categories", arraylist[p0].categories)
            intent.putExtra("percentage", arraylist[p0].percentage.toString())
            intent.putExtra("max_amount", arraylist[p0].max.toString())

            context.startActivity(intent)
        }

        return view
    }

    private fun remove(id: Int) {
        db.delete_Distribute(id)
        arraylist = db.getAll_Distribute()
    }

}

