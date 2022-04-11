package com.example.budgetapp

data class recurringExpense(var id: Int? = null,
                            var title: String = "",
                            var categories: String = "",
                            var amount: Double = 0.0,
                            var date: String = "",
                            var last_paid: String = "",
                            var is_paid: Boolean = false
                            )
