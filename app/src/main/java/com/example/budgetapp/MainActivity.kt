package com.example.budgetapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var paycheckAddButton: Button
    private lateinit var billAddButton: Button
    private lateinit var viewExpense: Button
    private lateinit var paycheckAmount: EditText
    private lateinit var billAmount: EditText
    private lateinit var jobInput: EditText
    private lateinit var billTitle: EditText
    private lateinit var dateBill: TextView
    private lateinit var datePaycheck: TextView

    private lateinit var dateButton: Button
    private lateinit var date1Button: Button
    var cal = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paycheckAddButton = findViewById(R.id.add_paycheck_button)
        billAddButton = findViewById(R.id.add_bill_button)
        viewExpense = findViewById(R.id.view_expense_button)
        paycheckAmount = findViewById(R.id.paycheck_amount)
        billAmount = findViewById(R.id.bill_amount)
        jobInput = findViewById(R.id.job_input)
        billTitle = findViewById(R.id.bill_title)
        dateBill = findViewById(R.id.date_input)
        datePaycheck = findViewById(R.id.date1_input)
        dateButton = findViewById(R.id.date_picker)
        date1Button = findViewById(R.id.date1_picker)

        dateBill!!.text = "--/--/----"
        datePaycheck!!.text = "--/--/----"

        val context = this
        val db = EntriesDB(context)


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewBill()
            }
        }

        val date1SetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewPaycheck()
            }
        }

        dateButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@MainActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })


        billAddButton.setOnClickListener {
            // save in database for amount and message
            if (billAmount.text != null && billAmount != null && billTitle != null) {
                val amount = billAmount.text.toString().toDouble()
                val title = billTitle.text.toString()
                val date = dateBill.text.toString()
                val category = "none"
                val newEntry = Entry(id = null, title, date, amount, category)
                db.insertData(newEntry)
            }
        }

        date1Button!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@MainActivity,
                    date1SetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })


        paycheckAddButton.setOnClickListener {
            if (paycheckAmount.text != null && jobInput != null && datePaycheck != null) {
                val amount = paycheckAmount.text.toString().toDouble()
                val title = jobInput.text.toString()
                val date = datePaycheck.text.toString()
                val category = "paycheck"
                val newEntry= Entry(id= null, title, date, amount, category)
                db.insertData(newEntry)
            }
        }

        viewExpense.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            startActivity(intent)
        }

        /***************************TEST*************************************************/
//        val d1 = Distribute(null, "Transportation", 0.2, 200.0)
//        val d2 = Distribute(null, "Food", 0.3, 250.0)
//        val d3 = Distribute(null, "Food", 0.2, 300.0)
//        val d4 = Distribute(null, "Entertain", 0.2, 200.0)
//        val d5 = Distribute(null, "Other", 0.2, 200.0)
//        db.deleteAll_Distribute()
//        val row1 = db.insert_Distribute(d1)
//        val row2 = db.insert_Distribute(d2)
//        val row3 = db.insert_Distribute(d3)
//        val row4 = db.insert_Distribute(d4)
//        val row5 = db.insert_Distribute(d5)
//
//
//        val myRow3 = db.getRow_Distribute(3)
//        db.updateRow_Distribute(1, Distribute(null, "Family", 0.2, 200.0))
//
//        val category = db.getCategories_Distribute()
//
//        val distributes = db.getAll_Distribute()

        val c1 = RecurringExpense(null, "Netflix", 39.9, "04/16/2022", "Entertain",
                                    "03/16/2022", false)
        val c2 = RecurringExpense(null, "Amazon", 29.9, "04/20/2022", "Living",
            "03/20/2022", false)

        val c3 = RecurringExpense(null, "Spotify", 9.9, "04/22/2022", "Entertain",
            "03/19/2022", true)
        db.deleteAll_Recurring()
        val row1 = db.insert_Recurring(c1)
        val row2 = db.insert_Recurring(c2)
        val row3 = db.insert_Recurring(c3)

        val result1 = db.getAll_Recurring()

        val new_c2 = RecurringExpense(null, "Amazon", 29.9, "04/30/2022", "Living",
            "03/20/2022", true)

        val new_row2 = db.updateRow_Recurring(2, new_c2)
        val result2 = db.getAll_Recurring()

        db.deleteRow_Recurring(1)
        val result3 = db.getAll_Recurring()



        println("nothing")


    }
    // end of onCreate

    private fun updateDateInViewBill() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateBill!!.text = sdf.format(cal.getTime())
    }
    private fun updateDateInViewPaycheck() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        datePaycheck!!.text = sdf.format(cal.getTime())
    }

}
