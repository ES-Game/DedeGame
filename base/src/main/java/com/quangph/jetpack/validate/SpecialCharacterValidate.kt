package com.quangph.jetpack.validate

import com.quangph.pattern.spec.AbstractSpecification
import java.util.regex.Pattern

class SpecialCharacterValidate(private val allowIncludeSpace: Boolean) :
    AbstractSpecification<String?>() {

    override fun isSatisfiedBy(specialChar: String?): Boolean {
        val pattern: Pattern = if (allowIncludeSpace) Pattern.compile("[^A-Za-z0-9\\s]") else Pattern.compile("[^A-Za-z0-9]")
        val match = pattern.matcher(specialChar)
        return match.find()
    }
}