package com.quangph.jetpack.validate

import com.quangph.pattern.spec.AbstractSpecification
import java.util.regex.Pattern

/**
 * Must remove all non-digit character before using this
 */
class PhoneNumberValidate : AbstractSpecification<String?>() {
    override fun isSatisfiedBy(phone: String?): Boolean {
        return phone?.isNotBlank() ?: false
                && Pattern.matches("[0][0-9]{9}$", phone)
    }
}