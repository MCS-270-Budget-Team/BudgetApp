package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.math.pow
import android.content.res.ColorStateList
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private lateinit var addEntryButton: ImageButton
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var upcomingBillButton: ImageButton

    private lateinit var experienceBar: ProgressBar
    private lateinit var earningBar: ProgressBar
    private lateinit var spendingBar: ProgressBar

    private lateinit var totalAmount: TextView
    private lateinit var goalListview: ListView
    private var goalAdapter: GoalAdapter? = null
    //create database object
    private val context = this
    private val db = EntriesDB(context)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addEntryButton = findViewById(R.id.add_entry_button)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)

        experienceBar = findViewById(R.id.experienceBar)
        earningBar = findViewById(R.id.earningBar)
        spendingBar = findViewById(R.id.spendingBar)

        totalAmount = findViewById(R.id.total_amount)
        val totalMoney = db.addPaycheckAmount() - db.addExpenseAmount()

        goalListview = findViewById(R.id.goal_listview)

        totalAmount.text = "Total Amount: $$totalMoney"

        adjustExpenseButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, AdjustExpense::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        val goalList = mutableListOf(Goal(null, "Invest $300 monthly", 5, 3),
            Goal(null, "Save $100 weekly", 10, 4),
            Goal(null, "Pay all the bills on time", 50, 9),
            Goal(null, "Spend $100 on yourself", 20, 6),
            Goal(null, "Eat out twice per week", 8, 3))

        goalAdapter = GoalAdapter(applicationContext, goalList)
        goalListview.adapter = goalAdapter
    }

}

class GoalAdapter(var context: Context, private var arraylist: MutableList<Goal>): BaseAdapter() {
//    private val db = EntriesDB(context)

    // parameter to calculate level's required experience points
    private val X: Double = 0.3
    private val Y: Double = 2.0

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
        val view: View = View.inflate(context, R.layout.activity_game_bubble, null)
        val title: Button = view.findViewById(R.id.goal_button)
        val progressBar: ProgressBar = view.findViewById(R.id.level_progress)
        val level: TextView = view.findViewById(R.id.level)
        val plus: ImageButton = view.findViewById(R.id.plus)

        val goal: Goal = arraylist[p0]

        title.text = goal.title
        level.text = "Level ${goal.level}"

        //calculating the progress bar percentage
        val current_level_progress = goal.plus * 20 - findLevelExp(goal.level)
        val level_difference = findLevelExp(goal.level + 1) - findLevelExp(goal.level)
        val progress = (current_level_progress / level_difference * 100).toInt()
        progressBar.progress = progress

        //getting and setting the color of the bubble based on the level
        val goalColor = findProgressColor(goal.level)
        progressBar.progressTintList = ColorStateList.valueOf(Color.parseColor(goalColor))
        plus.setBackgroundColor(Color.parseColor(goalColor))
        plus.backgroundTintList = ColorStateList.valueOf(Color.parseColor(goalColor))

        //updating the bubble and the level when the user press the plus icon
        return view
    }

    private fun findLevelExp(level: Int): Double{
        return (level / X).pow(Y)
    }

    private fun findProgressColor(level: Int): String{
        val colorList = arrayOf("#8144EF", "#EA7D5B", "#ED4981", "#A9EC5C", "#5DD5E4")

        if (level <= 3){
            return colorList[0]
        }
        else if (level <= 7){
            return colorList[1]
        }
        else if (level <= 12){
            return colorList[2]
        }
        else if (level <= 20){
            return colorList[3]
        }
        else{
            return colorList[4]
        }
    }

}
