package com.quangph.jetpack.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object NumberFormatter {
    const val PATTERN_NUMBER_THREE_RIGHT_DOT = "#,##0.###"
    const val PATTERN_NUMBER_TWO_RIGHT_DOT = "#,##0.##"
    const val PATTERN_NUMBER_ONE_RIGHT_DOT = "#,##0.#"
    const val PATTERN_NUMBER_NON_RIGHT_DOT = "#,##0"

    fun formatNumber(number: Double, place: Int = 3): String {
        val symbols = DecimalFormatSymbols()
        symbols.groupingSeparator = ','
        symbols.decimalSeparator = '.'
        val formatter = DecimalFormat("#,##0.###", symbols)
        formatter.roundingMode = RoundingMode.HALF_UP
        formatter.groupingSize = 3
        formatter.maximumFractionDigits = place
        return formatter.format(number)
    }

    fun formatNumberAndRound(
            number: Double,
            place: Int? = null,
            roundingMode: RoundingMode? = RoundingMode.HALF_UP,
            formatPattern: String = PATTERN_NUMBER_THREE_RIGHT_DOT
    ): String {
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = ','
        }
        symbols.decimalSeparator = '.'
        val formatter = DecimalFormat(formatPattern, symbols)
        formatter.groupingSize = 3
        if (roundingMode != null) {
            formatter.roundingMode = roundingMode
        }
        if (place != null) {
            formatter.maximumFractionDigits = place
        }
        return formatter.format(number)
    }

    fun formatMoney(number: Double): String {
        return formatNumberAndRound(number, formatPattern = PATTERN_NUMBER_NON_RIGHT_DOT)
    }

    fun formatStringNumberToNumber(number: String): String {
        return number.replace(",", "")
    }
}