package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ExpenseViewAdapter(var context: Context, private var arraylist: MutableList<Entry>): BaseAdapter() {
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

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


        val view: View = View.inflate(context, R.layout.expense_bubble_activity, null)

//        val title: TextView = view.findViewById(R.id.title)
        val categories: TextView = view.findViewById(R.id.categories)
        val date: TextView = view.findViewById(R.id.date)
        val amount: TextView = view.findViewById(R.id.amount)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        val editButton: Button = view.findViewById(R.id.edit_button)

        val expense: Entry = arraylist[p0]

        categories.text = expense.title
        date.text = expense.date
        amount.text = "$${expense.amount.toInt()}"

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

        editButton.setOnClickListener{
            val intent = Intent(context, EditEntries::class.java)
//            intent.putExtra("newExpense", newExpense
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", arraylist[p0].id)
            intent.putExtra("title", arraylist[p0].title)
            intent.putExtra("categories", arraylist[p0].categories)
            intent.putExtra("amount", arraylist[p0].amount.toString())
            intent.putExtra("date", arraylist[p0].date)

            context.startActivity(intent)
        }
        return view
    }

    private fun remove(id: Int, position: Int) {
        entryDB.deleteData(id)
        arraylist.remove(arraylist[position])
    }

}