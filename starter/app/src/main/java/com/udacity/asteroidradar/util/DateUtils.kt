package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Isolate the Util method for better re-usability
 *
 * If not calender obj is passed, returns `API_QUERY_DATE_FORMAT` for today's date
 */
fun getFormattedDateString(
    calendar: Calendar = Calendar.getInstance()
) : String {
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

fun getNDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    // [today, today + 7 - 1]
    for (i in 0 until Constants.DEFAULT_END_DATE_DAYS) {
        formattedDateList.add(getFormattedDateString(calendar))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
