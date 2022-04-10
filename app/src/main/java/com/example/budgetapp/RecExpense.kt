package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RecExpense: AppCompatActivity() {

    private lateinit var adjustButton: Button
    private lateinit var recList: ListView
    private var recAdapter: RecAdapter? = null
    private var expenseBank: ArrayList<Expense>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reccommend_expense)

        adjustButton = findViewById(R.id.adjust_btn)
        recList = findViewById(R.id.rec_listview)

        adjustButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@RecExpense, AdjustExpense::class.java) //
            // start new activity without transition animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        expenseBank = arrayListOf(
            Expense(null, "housing", 30.0, 2000.0),
            Expense(null, "food", 10.0, 500.0),
            Expense(null, "utilities", 5.0, 100.0),
            Expense(null, "transportation", 10.0, 500.0),
            Expense(null, "others", 15.0, 1000.0),
            Expense(null, "savings", 30.0, 3000.0),
        )

        recAdapter = RecAdapter(applicationContext, expenseBank!!)
        recList?.adapter = recAdapter
    }

}