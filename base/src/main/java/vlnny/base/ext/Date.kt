package vlnny.base.ext


import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDate(): Date =
    Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

val currentDate : LocalDateTime = LocalDateTime.now()

fun getDate(deltaFromCurrentDate : Long) =
    currentDate.minusDays(deltaFromCurrentDate).toDate()