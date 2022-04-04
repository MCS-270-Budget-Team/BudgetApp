package com.example.budgetapp
// date in format mm/dd/yyyy
data class Paycheck(var date: String = "01/22/2022",
                   var amount: Double = 0.1,
                   var origin: String = "Job")