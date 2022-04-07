package com.example.budgetapp

// date format mm/dd/yyyy
data class Entry( var id: Int? = null,
                    var title: String = "Empty Title",
                   var date: String = "01/22/2022",
                   var amount: Double = 0.1,
                   var categories: String = "None")