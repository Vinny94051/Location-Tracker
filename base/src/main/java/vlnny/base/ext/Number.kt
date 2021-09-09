package vlnny.base.ext

import android.content.res.Resources
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.abs


fun Int.toDp() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toSp() = (this.toDp() / Resources.getSystem().displayMetrics.scaledDensity)

fun Int.length() = when(this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

/*
* Round double value for [decimals] numbers after dot.
* If last number '0' replace it on '1'
* */
fun Double.round(decimals: Int): Double {
    var decimalsNumber = ""
    for (i in 0 until decimals) decimalsNumber += "0"

    var integerNumber = ""
    for (i in 0 until toInt().length()) integerNumber += "#"

    val df = DecimalFormat("$integerNumber.$decimalsNumber")
    var doubleInString = df.format(this).replaceFirst(',', '.')

    if (doubleInString.last() == '0') doubleInString = doubleInString.replaceLastChar('1')

    return doubleInString.toDouble()
}