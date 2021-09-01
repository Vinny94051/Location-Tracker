package vlnny.base.ext

import android.text.Editable
import android.text.SpannableStringBuilder

fun String.toEditable(): Editable = SpannableStringBuilder(this)