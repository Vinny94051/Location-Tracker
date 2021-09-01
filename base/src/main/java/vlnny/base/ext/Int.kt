package vlnny.base.ext

import android.content.res.Resources
import com.google.android.material.internal.ViewUtils.dpToPx




fun Int.toDp() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toSp() = (this.toDp() / Resources.getSystem().displayMetrics.scaledDensity)

