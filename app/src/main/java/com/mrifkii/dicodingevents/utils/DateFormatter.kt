package com.mrifkii.dicodingevents.utils

import java.text.SimpleDateFormat
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
}