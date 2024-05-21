package com.dede.dedegame.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.presentation.common.LogUtil
import com.quangph.base.common.ActivityNavi
import com.quangph.jetpack.IScreenData
import com.quangph.jetpack.JetActivity

inline fun <reified ACTIVITY : Activity> JetActivity<*>.navigate(
    input: IScreenData?,
    onResult: ActivityNavi.OnActivityResult<ActivityResult>?
) {
    val itn = Intent(this, ACTIVITY::class.java)
    if (input != null) {
        itn.putExtra(input.javaClass.name, input)
    }
    launch(itn, onResult)
}


inline fun <reified ACTIVITY : Activity> JetActivity<*>.navigate(input: IScreenData?) {
    navigate<ACTIVITY>(input, null)
}

inline fun <reified Input : IScreenData> JetActivity<*>.getInput(): Input? {
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra(Input::class.java.name, Input::class.java)
//        } else {
//            intent.getParcelableExtra(Input::class.java.name)
//        }

    return intent.getParcelableExtra(Input::class.java.name)
}

inline fun <reified Output : IScreenData> Intent.getOutput(): Output? {
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            return getParcelableExtra(Output::class.java.name, Output::class.java)
//        } else {
//            return getParcelableExtra(Output::class.java.name)
//        }

    return getParcelableExtra(Output::class.java.name)
}

inline fun <reified Output : IScreenData> returnData(data: Output): Intent {
    val itn = Intent()
    itn.putExtra(Output::class.java.name, data)
    return itn
}

fun Context.startActivity(activityClass: Class<out Activity>, extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, activityClass)
    intent.extras()
    startActivity(intent)
}

fun String.hasLeastOneUpperCase(): Boolean {
    val regularExpressionForPassword = ".*[A-Z]+.*"
    val stringInput = Regex(regularExpressionForPassword)
    return stringInput.containsMatchIn(this)
}

fun String.hasLeastOneLowerCase(): Boolean {
    val regularExpressionForPassword = ".*[a-z]+.*"
    val stringInput = Regex(regularExpressionForPassword)
    return stringInput.containsMatchIn(this)
}

fun String.hasLeastOneSpecialCharacter(): Boolean {
    val specialCharacters = setOf(
        '!',
        '"',
        '#',
        '$',
        '%',
        '&',
        '\'',
        '(',
        ')',
        '*',
        '+',
        ',',
        '-',
        '.',
        '/',
        ':',
        ';',
        '<',
        '=',
        '>',
        '?',
        '@',
        '[',
        '\\',
        ']',
        '^',
        '_',
        '`',
        '{',
        '|',
        '}',
        '~'
    )
    return any { it in specialCharacters }
}

fun String.checkMinium6Length(): Boolean {
    val regularExpressionForPassword = ".{6,}$"
    val stringInput = Regex(regularExpressionForPassword)
    return stringInput.matches(this)
}

fun String.validateEmail(): Boolean {
    val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"
    return Regex(emailRegex).matches(this)
}

fun String.stringToEnum(): Slider.Type? {
    return try {
        Slider.Type.valueOf(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        Glide.with(this@loadImageFromUrl.context)
            .load(it)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    this@loadImageFromUrl.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}

fun RecyclerView.removeItemDecorations() {
    while (this.itemDecorationCount > 0) {
        this.removeItemDecorationAt(0)
    }
}