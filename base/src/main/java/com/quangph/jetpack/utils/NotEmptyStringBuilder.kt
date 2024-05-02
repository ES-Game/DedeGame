package com.quangph.jetpack.utils

/**
 * Build str by many pieces. If a piece is null, it will not be added
 * Created by QuangPH on 2020-03-18.
 */
class NotEmptyStringBuilder(private val split: String) {

    private var result: String? = null

    fun append(str: String?): NotEmptyStringBuilder {
        if (str.isNullOrEmpty()) {
            return this
        }

        if (result == null) {
            result = str
        } else {
            result += split + str
        }

        return this
    }

    fun build(): String? {
        return result
    }
}