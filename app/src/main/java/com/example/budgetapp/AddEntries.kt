package com.example.budgetapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class AddEntries : AppCompatActivity() {

    private lateinit var paycheckAddButton: Button
    private lateinit var billAddButton: Button
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
        setContentView(R.layout.add_expense_activity)
        paycheckAddButton = findViewById(R.id.add_paycheck_button)
        billAddButton = findViewById(R.id.add_bill_button)
        paycheckAmount = findViewById(R.id.paycheck_amount)
        billAmount = findViewById(R.id.bill_amount)
        jobInput = findViewById(R.id.job_input)
        billTitle = findViewById(R.id.bill_title)
        dateBill = findViewById(R.id.date_input)
        datePaycheck = findViewById(R.id.date1_input)
        dateButton = findViewById(R.id.date_picker)
        date1Button = findViewById(R.id.date1_picker)

        dateBill.text = "--/--/----"
        datePaycheck.text = "--/--/----"

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

        dateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@AddEntries,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })


        //connect recurring options to spinner
        val spinner: Spinner = findViewById(R.id.spinner1)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.recurring_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        billAddButton.setOnClickListener {
            // save in database for amount and message
            if (billAmount.text != null && billAmount != null && billTitle != null) {
                //check if amount is numeric
                if(!isNumeric(billAmount.text.toString())){
                    val toast = Toast.makeText(this, "Amount must be numeric!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()                }
                else{
                    val recurringOption = spinner.selectedItem.toString()
                    val amount = billAmount.text.toString().toDouble()
                    val title = billTitle.text.toString()
                    val date = dateBill.text.toString()
                    val category = "expense"
                    if (recurringOption != "Not Recurring") {
                        val newRecurring = RecurringExpense(null, title, amount, date, category, date, true)
                        // add newRecurring to recurring expense database
                    }
                }
            }
        }

        date1Button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddEntries,
                    date1SetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })


        paycheckAddButton.setOnClickListener {
            if (paycheckAmount.text != null && jobInput != null && datePaycheck != null) {
                val title = jobInput.text.toString()
                val date = datePaycheck.text.toString()

                // check if amount is numeric
                if (!isNumeric(paycheckAmount.text.toString())){
                    val toast = Toast.makeText(this, "Amount must be numeric!", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
                else {
                    val amount = paycheckAmount.text.toString().toDouble()
                    val category = "paycheck"
                    val newEntry = Entry(id = null, title, date, amount, category)
                    db.insertData(newEntry)
                }
            }
        }
    }
    // end of onCreate

    private fun updateDateInViewBill() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        sdf.format(cal.getTime()).also { dateBill.text = it }
    }
    private fun updateDateInViewPaycheck() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        sdf.format(cal.getTime()).also { datePaycheck.text = it }
    }

    /* Function to check whether a string is numeric*/
    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }
}
