package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*


class ExpenseViewAdapter(var context: Context, var arraylist: MutableList<Entry>): BaseAdapter() {
    private val entryDB = EntriesDB(context)
    private val visibilityList = MutableList<Boolean>(arraylist.size){true}


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


        val view: View = View.inflate(context, R.layout.expense_bubble_activity, null)

        val categoryViewSwitcher = view.findViewById(R.id.viewswitcher_categories) as ViewSwitcher
        val amountViewSwitcher = view.findViewById(R.id.viewswitcher_amount) as ViewSwitcher
        val buttonViewSwitcher = view.findViewById(R.id.viewswitcher_button) as ViewSwitcher

        var categories: TextView = view.findViewById(R.id.categories)
        val date: TextView = view.findViewById(R.id.date)
        var amount: TextView = view.findViewById(R.id.amount)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        var editButton: Button = view.findViewById(R.id.edit_button)
        var categoriesEdit: EditText = view.findViewById(R.id.categories_edit)
        var amountEdit: EditText = view.findViewById(R.id.amount_edit)
        val addButton: Button = view.findViewById(R.id.add_button)

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
            categoryViewSwitcher.showNext()
            amountViewSwitcher.showNext()
            buttonViewSwitcher.showNext()
        }

        addButton.setOnClickListener{
            categoryViewSwitcher.showPrevious()
            amountViewSwitcher.showPrevious()
            buttonViewSwitcher.showPrevious()
        }


        return view
    }

    fun remove(id: Int, position: Int) {
        entryDB.deleteData(id)
        arraylist.remove(arraylist.get(position))
    }

}

