package com.uonagent.supermagazin.utils

import java.text.DecimalFormat

private const val CURR = "BYN"

object CurrencyFormatter {
    fun doubleToString(value: Double) =
            DecimalFormat("#0.00").format(value).toString() + " $CURR"

    fun stringToValueString(string: String): String {
        val sub = string.substring(0, string.length - CURR.length - 1)

        return DecimalFormat("#0.00").format(sub.toDouble()).toString()
    }

    fun valueStringToValue(string: String) = string.toDouble()
}