package com.mrifkii.dicodingevents.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    fun formatEventDate(dateString: String?): String {
        if (dateString == null) return ""

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    fun isEventPast(dateString: String?): Boolean {
        if (dateString == null) return true
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val eventDate = sdf.parse(dateString)
            val currentDate = Date()

            eventDate?.before(currentDate) ?: true
        } catch (e: Exception) {
            true
        }
    }
}