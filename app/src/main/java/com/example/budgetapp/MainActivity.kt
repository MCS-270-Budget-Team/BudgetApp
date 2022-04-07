package com.example.budgetapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var datePaycheck: EditText

    private lateinit var dateButton: Button
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

        dateBill!!.text = "--/--/----"

        val context = this
        val db = ExpenseDB(context)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
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
                val message = billTitle.text.toString()
                val date = dateBill.text.toString()
                //val newExpense = Expense(message, date, amount)
                //db.insertData(newExpense)
            }
        }

        paycheckAddButton.setOnClickListener {
            if (paycheckAmount.text != null && jobInput != null && datePaycheck != null) {
                val amount = paycheckAmount.text.toString()
                val message = jobInput.text.toString()
                val date = datePaycheck.text.toString()
            }

            // save in database for amount, date, and bill title
        }

        viewExpense.setOnClickListener {
            // start new activity
            // calc budget
            val intent = Intent(this@MainActivity, ExpenseViewer::class.java) //
            startActivity(intent)
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateBill!!.text = sdf.format(cal.getTime())
    }
}