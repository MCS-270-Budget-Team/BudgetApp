package com.example.budgetapp

import java.io.Serializable

data class Expense (var id: Int? = null,
                    var categories: String = "",
                    var percentage: Double = 0.0,
                    var max: Double = 0.0
                    ): Serializable