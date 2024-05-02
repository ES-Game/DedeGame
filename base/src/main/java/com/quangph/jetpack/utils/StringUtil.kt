package com.quangph.jetpack.utils

import android.text.TextUtils
import com.quangph.jetpack.validate.PhoneNumberValidate
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Created by QuangPH on 2020-11-28.
 */

object StringUtil {

    private val SOURCE_CHARACTERS = charArrayOf('À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự', 'Ỳ', 'ỳ', 'Ỷ', 'ỷ', 'Ỹ', 'ỹ', 'Ỵ', 'ỵ')

    // Normalized characters
    private val DESTINATION_CHARACTERS = charArrayOf('A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u', 'Y', 'y', 'Y', 'y', 'Y', 'y', 'Y', 'y')

    /**
     * Make the first char of str to uppercase
     */
    fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }


    fun removeAccent(target: CharSequence?): String? {
        if (target.isNullOrBlank()) return ""
        val sb = StringBuilder(target)
        for (i in sb.indices) {
            sb.setCharAt(i, removeAccent(sb[i]))
        }
        return sb.toString()
    }

    fun removeAccent(target: Char): Char {
        val index = SOURCE_CHARACTERS.indexOfFirst { it == target }
        return when {
            index == -1 -> target
            else -> DESTINATION_CHARACTERS[index]
        }
    }

    /**
     * replace non alphabet character by replaced character
     * @param target
     * @param replace
     * @return
     */
    fun replaceNonAlphabet(target: CharSequence?, replace: String): String? {
        return target?.replace(Regex("[^A-Za-z0-9]"), replace)
    }

    /**
     * replace same character side by side by one
     * @param target
     * @param replace
     * @return
     */
    fun replaceDuplicateCharacter(target: CharSequence?, replace: String): String? {
        return target?.replace(Regex("${replace}+"), replace)
    }

    /**
     * Compare two strings in which we do not take care of accent
     */
    fun compare(source: String?, destination: String?): Boolean {
        val sourceRemoveAccent = removeAccent(source)
        val destinationRemoveAccent = removeAccent(destination)
        if (sourceRemoveAccent == null || destinationRemoveAccent == null) {
            return false
        }
        sourceRemoveAccent.replace(" ", "")
        destinationRemoveAccent.replace(" ", "")
        return sourceRemoveAccent.toUpperCase() == destinationRemoveAccent.toUpperCase()
    }

    /**
     * Encrypt string to MD5
     */
    fun hashMd5(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("md5")
            digest.update(input.toByteArray())
            val bytes = digest.digest()
            val sb = java.lang.StringBuilder()
            for (i in bytes.indices) {
                sb.append(String.format("%02X", bytes[i]))
            }
            sb.toString().toLowerCase()
        } catch (exc: Exception) {
            "" // Impossibru!
        }
    }

    /**
     * Extract all phone numbers in a string
     */
    fun getPhoneNumberList(input: String): List<String> {
        val digitsStr: String = input.replace("[^0-9]", " ")
        val strSplitList = digitsStr.split(" ")
        val phoneNumberList = mutableListOf<String>()
        strSplitList.forEach {
            val isPhoneNumber = PhoneNumberValidate().isSatisfiedBy(it)
            if (isPhoneNumber) {
                phoneNumberList.add(it)
            }
        }
        return phoneNumberList
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###,###")
        return formatter.format(amount.toDouble())
    }
}