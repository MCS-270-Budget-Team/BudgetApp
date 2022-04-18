package com.example.budgetapp

import java.time.temporal.TemporalAmount

data class RecurringExpense(var id: Int? = null,
                            var title: String = "title",
                            var amount: Double = 0.1,
                            var date: String = "04/11/2022",
                            var categories: String = "Living",
                            var last_paid: String = "03/11/2022",
                            var is_paid: Boolean = false)