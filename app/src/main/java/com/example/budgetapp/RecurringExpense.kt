package com.example.budgetapp

data class RecurringExpense(var id: Int? = null,
                            var title: String = "",
                            var amount: Double = 0.0,
                            var date: String = "",
                            var categories: String = "expense",
                            var last_paid: String = "",
                            var is_paid: Boolean = false)