package com.example.budgetapp

import java.io.Serializable

data class Expense (var id: Int? = null,
                    var categories: String = "",
                    var percentage: Int = 0,
                    var max: Int = 0
                    ): Serializable