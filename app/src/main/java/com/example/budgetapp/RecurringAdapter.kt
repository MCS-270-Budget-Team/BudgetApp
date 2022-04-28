package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
        val editButton: ImageButton = view.findViewById(R.id.recurring_edit)
        val nextDateToPayOn: TextView = view.findViewById(R.id.recurring_date)

        val expense: RecurringExpense = arraylist[p0]

        name.text = expense.title
        val amountString = expense.amount.toString()
        amount.text = String.format("\$%.2f", amountString)
        nextDateToPayOn.text = expense.date
        category.text = expense.categories

        deleteButton.setOnClickListener {
            // start new activity
            // calc budget
            remove(expense.id!!, p0)
            notifyDataSetChanged()
            Toast.makeText(context, "Successfully deleted recurring expense!", Toast.LENGTH_SHORT).show()
        }
        editButton.setOnClickListener {
            val intent = Intent(context, EditRecurringBill::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", arraylist[p0].id)
            intent.putExtra("title", arraylist[p0].title)
            intent.putExtra("categories", arraylist[p0].categories)
            intent.putExtra("frequency", arraylist[p0].frequency)
            intent.putExtra("amount", arraylist[p0].amount.toString())
            intent.putExtra("date", arraylist[p0].date)

            context.startActivity(intent)
        }

        return view
    }

    private fun remove(id: Int, position: Int) {
        entryDB.deleteRow_Recurring(id)
        arraylist.remove(arraylist.get(position))
    }

}

class EditRecurringBill : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var title: EditText
    private lateinit var frequency: Spinner
    private lateinit var categories: Spinner
    private lateinit var amount: EditText
    private lateinit var date: EditText
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    private var selectedCategories: String = ""
    private var selectedFrequency: String = ""
    private val categoriesOption =
        arrayOf("paycheck", "expense") //This should draw from user-defined categories
    private val frequencyOptions = arrayOf("Weekly", "Monthly", "Annually")

    //create database object
    private val context = this
    private val db = EntriesDB(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recurring)

        categories = findViewById(R.id.categories)
        frequency = findViewById(R.id.frequency_spinner)
        amount = findViewById(R.id.amount)
        title = findViewById(R.id.title)
        date = findViewById(R.id.date)
        editButton = findViewById(R.id.edit)
        cancelButton = findViewById(R.id.cancel)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesOption)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categories.adapter = adapter
        categories.onItemSelectedListener = context

        val frequencyAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frequency.adapter = frequencyAdapter
        frequency.onItemSelectedListener = context

        val extras = intent.extras
        val id = extras!!.getInt("id")
        val prevTitle = extras.getString("title")
        val prevDate = extras.getString("date")
        val prevCategory = extras.getString("categories")
        val prevFrequency = extras.getString("frequency")
        val prevAmount = extras.getString("amount")
        title.setText(prevTitle)
        date.setText(prevDate)
        amount.setText(prevAmount)
        categories.setSelection(categoriesOption.indexOf(prevCategory))
        frequency.setSelection(frequencyOptions.indexOf(prevFrequency))
        selectedFrequency = prevFrequency!!

        editButton.setOnClickListener {
            // Should probably prevent the user from creating recurring bills due in the past

            if (!isNumeric(amount.text.toString())) {
                val toast = Toast.makeText(
                    this,
                    "Amount must be numeric. Try again!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            } else if (!isValidDate(date.text.toString())) {
                val toast = Toast.makeText(
                    this,
                    "Date format is invalid. Try again!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            } else if (title.text.toString() == "") {
                val toast = Toast.makeText(
                    this,
                    "Title cannot be empty. Try again!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            } else if (selectedCategories == "") {
                val toast = Toast.makeText(
                    this,
                    "Categories cannot be empty. Try again!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            } else if (title.text.toString().length > 10) {
                val toast = Toast.makeText(
                    this,
                    "Title cannot exceed 10 characters. Try again!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
                toast.show()
            } else {
                if (selectedFrequency == "") selectedFrequency =
                    "Monthly" //Default to monthly if user doesn't specify

                val updatedEntry = RecurringExpense(
                    id,
                    title.text.toString(),
                    amount.text.toString().toDouble(),
                    date.text.toString(),
                    selectedCategories,
                    "",
                    selectedFrequency,
                )
                db.updateRow_Recurring(id, updatedEntry)
                updateRecurringBills()
                val intent = Intent(this, RecurringViewer::class.java)
                startActivity(intent)
            }
        }

        // if the user does not want to add anything, let them return to the homepage
        cancelButton.setOnClickListener {
            val intent = Intent(this, RecurringViewer::class.java)
            startActivity(intent)
        }

    }

    private fun updateRecurringBills() {
        val today = Calendar.getInstance()
        //Check to see if any of the recurring bills have passed their deadlines
        val recurringBills = db.getAll_Recurring()

        for(bill in recurringBills) {
            var dueDate = getDate(bill)

            while(today.after(dueDate)) {
                val entry = Entry(
                    id = null,
                    title = bill.title,
                    date = bill.date,
                    amount = bill.amount,
                    categories = bill.categories
                )

                db.insertData(entry)
                val newDate = getNewDate(dueDate, bill.frequency)
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                bill.last_paid = bill.date
                bill.date = sdf.format(newDate.time)
                db.updateRow_Recurring(bill.id, bill)
                dueDate = newDate
            }
        }
    }

    private fun getDate(expense: RecurringExpense): Calendar {
        val dueDate = Calendar.getInstance()
        val dateString = expense.date
        val tokens = dateString.split("/")

        if(tokens.size < 3) return dueDate
        dueDate.set(tokens[2].toInt(), tokens[0].toInt()-1, tokens[1].toInt())
        //Minus 1s offset from 1-12 to 0-11 for the month

        return dueDate
    }

    private fun getNewDate(oldDate: Calendar, frequency: String): Calendar {

        when (frequency) {
            "Weekly" -> {
                oldDate.add(Calendar.DATE, 7)
            }
            "Monthly" -> {
                oldDate.add(Calendar.MONTH, 1)
            }
            "Annually" -> {
                oldDate.add(Calendar.YEAR, 1)
            }
        }

        return oldDate
    }

    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    @SuppressLint("ResourceType")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        //Update the correct variable depending on the id of the spinner that was selected
        when (p0!!.id) {
            R.id.categories -> selectedCategories = categoriesOption[position]
            R.id.frequency_spinner -> selectedFrequency = frequencyOptions[position]
            else -> {
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        val toast = Toast.makeText(this, "Must select a categories!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)
        toast.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(inDate: String): Boolean {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            return false
        }
        return true
    }
}


