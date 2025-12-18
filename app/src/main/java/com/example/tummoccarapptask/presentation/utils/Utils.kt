package com.example.tummoccarapptask.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period


@RequiresApi(Build.VERSION_CODES.O)
fun calculateYearDifference(
    birthYear: Int
): String {
    val startDate = LocalDate.of(birthYear, 1, 1)
    val currentDate = LocalDate.now()
    return if (startDate.isAfter(currentDate)) {
        return ""
    } else {
        val period = Period.between(startDate, currentDate)
        "${period.years} years ${period.months} months"
    }

}