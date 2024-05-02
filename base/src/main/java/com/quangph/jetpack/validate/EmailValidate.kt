package com.quangph.jetpack.validate

import android.util.Patterns
import com.quangph.pattern.spec.AbstractSpecification

class EmailValidate : AbstractSpecification<String>() {

    override fun isSatisfiedBy(email: String?): Boolean {
        return email != null
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}