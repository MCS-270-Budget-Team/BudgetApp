package com.example.budgetapp

data class Distribute( var id: Int? = null,
                       var category: String = "None",
                       var percentage: Double = 0.0,
                       var max_amount: Double = 100.1)