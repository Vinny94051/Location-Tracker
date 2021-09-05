package vlnny.base.ext


import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDate(): Date =
    Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

fun LocalDate.toDate(): Date =
    this.atStartOfDay().toDate()

val currentDate : LocalDateTime = LocalDateTime.now()

fun getDate(deltaFromCurrentDate : Long) =
    currentDate.minusDays(deltaFromCurrentDate).toDate()