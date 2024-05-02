package com.quangph.jetpack.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.min

/**
 * Created by QuangPH on 2020-04-27.
 */
object NumberUtil {
    fun round(value: Double, places: Int): Double {
        require(places >= 0) { "Decimal-places must be greater than or equals zero" }
        return BigDecimal(value).setScale(places, RoundingMode.HALF_UP).toDouble()
    }

    fun parseDouble(value: String?, maximum: Double? = null, numberRightDot: Int = 3): Double {
        var numberString = value ?: "0.0"
        numberString = numberString.replace(",", "")
        if (numberString.contains(".")) {
            val textRightDot = numberString.split(".").last()
            if (textRightDot.length > numberRightDot) {
                numberString = numberString.substring(0, numberString.length - 1)
            }
        }
        val result = numberString.toDoubleOrNull() ?: 0.0
        if (maximum != null) {
            return min(result, maximum)
        }
        return result
    }

    fun parseToLong(value: String?): Long? {
        return try {
             value?.toLongOrNull()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun percentFormatter(percent: Float): String{
        val symbols = DecimalFormatSymbols()
        symbols.groupingSeparator = ','
        symbols.decimalSeparator = '.'

        val numberFormatter = DecimalFormat("#,##0", symbols)
        numberFormatter.roundingMode = RoundingMode.HALF_UP
        numberFormatter.groupingSize = 3
        return numberFormatter.format(percent)
    }
}