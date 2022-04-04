package com.example.budgetapp

data class Expense(var id: Int = 0,
                    var title: String = "Empty Title",
                   var date: String = "1/2/2022",
                   var amount: String = "0",
                   var categories: String = "None")