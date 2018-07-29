package com.uonagent.supermagazin.utils

import java.text.DecimalFormat

object CurrencyFormatter {
    fun doubleToString(value: Double) =
            DecimalFormat("#0.00").format(value).toString() + " BYN"
}