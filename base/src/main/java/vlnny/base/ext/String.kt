package vlnny.base.ext

import android.text.Editable
import android.text.SpannableStringBuilder

fun String.toEditable(): Editable = SpannableStringBuilder(this)

fun String.replaceLastChar(newChar: Char): String {
    return if (isNotEmpty()) substring(0, length - 1).plus(newChar) else this
}