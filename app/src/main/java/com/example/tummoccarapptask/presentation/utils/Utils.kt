package com.example.tummoccarapptask.presentation.utils


import android.util.Log
import java.time.LocalDate
import java.time.Period


fun calculateYearDifference(
    birthYear: Int
): String {
    try {
        val startDate = LocalDate.of(birthYear, 1, 1)
        val currentDate = LocalDate.now()
        return if (startDate.isAfter(currentDate)) {
            return ""
        } else {
            val period = Period.between(startDate, currentDate)
            "${period.years} years ${period.months} months"
        }
    } catch (e: Exception) {
        Log.e("DEBUG", "Exception " + e.localizedMessage)
        return ""
    }

}