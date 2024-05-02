package com.quangph.jetpack.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.absoluteValue

object MoneyFormatter {
    fun formatMoney(number: Double): String {
        val value = if (number in -0.5 .. 0.0) {
            0.0
        } else {
            number
        }
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = ','
        }
        val formatter = DecimalFormat("#,##0", symbols)
        formatter.roundingMode = RoundingMode.HALF_UP
        formatter.groupingSize = 3
        return formatter.format(value)
    }

    fun formatMoneyAndRound(number: Double): String {
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = ','
        }
        val formatter = DecimalFormat("#,##0", symbols)
        formatter.roundingMode = RoundingMode.HALF_UP
        formatter.groupingSize = 3
        return formatter.format(number)
    }

    fun formatMoneyNoSymbols(number: Double): String {
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = ','
            decimalSeparator = '.'
            monetaryDecimalSeparator = '.'
        }
        val formatter = DecimalFormat("#,###", symbols)
        return formatter.format(number)
    }

    fun formatMoneyWithSuffixAndRound(number: Double, thousand: String, million: String, billion: String): String {
        val suffixes: NavigableMap<Double, String> = TreeMap()
        suffixes.apply {
            suffixes[1_000.0] = thousand
            suffixes[1_000_000.0] = million
            suffixes[1_000_000_000.0] =  billion
        }
        if (number < 0) {
            return "-" + formatMoneyWithSuffixAndRound(number.absoluteValue, thousand, million, billion)
        }
        if (number <= 1_000_000) {
            return formatMoney(number)
        }
        val validSuffixMap = suffixes.floorEntry(number)
        val divideBy = validSuffixMap.key
        val suffix = validSuffixMap.value
        val truncatedNumber = NumberUtil.round(number / divideBy, 2)
        return "${NumberFormatter.formatNumber(truncatedNumber)} $suffix"
    }

    fun formatMoneyWithSuffixAndRoundForDashboardReport(number: Double, thousand: String, million: String, billion: String): String {
        val suffixes: NavigableMap<Double, String> = TreeMap()
        suffixes.apply {
            suffixes[1_000.0] = thousand
            suffixes[1_000_000.0] = million
            suffixes[1_000_000_000.0] =  billion
        }
        if (number < 0) {
            return "-" + formatMoneyWithSuffixAndRoundForDashboardReport(number.absoluteValue, thousand, million, billion)
        }
        if (number < 1_000_000_000) {
            return formatMoney(number)
        }
        val validSuffixMap = suffixes.floorEntry(number)
        val divideBy = validSuffixMap.key
        val suffix = validSuffixMap.value
        val truncatedNumber = NumberUtil.round(number / divideBy, 2)
        return "${NumberFormatter.formatNumber(truncatedNumber)} $suffix"
    }
}