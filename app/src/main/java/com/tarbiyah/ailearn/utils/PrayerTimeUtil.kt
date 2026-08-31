package com.tarbiyah.ailearn.utils

import com.tarbiyah.ailearn.data.model.PrayerTimings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object PrayerTimeUtil {

    data class NextPrayer(
        val name: String,
        val time: String,
        val isTomorrow: Boolean
    )

    /**
     * Determines the next prayer based on current time.
     */
    fun getNextPrayer(timings: PrayerTimings): NextPrayer {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeStr = sdf.format(Date())
        
        // List of prayers in order
        val prayers = listOf(
            Pair("SUBUH", timings.fajr),
            Pair("DZUHUR", timings.dhuhr),
            Pair("ASHAR", timings.asr),
            Pair("MAGHRIB", timings.maghrib),
            Pair("ISYA", timings.isha)
        )

        for (prayer in prayers) {
            // Clean time string just in case it contains timezone (e.g., "12:30 (WIB)")
            val cleanTime = prayer.second.take(5)
            
            if (currentTimeStr < cleanTime) {
                return NextPrayer(prayer.first, cleanTime, false)
            }
        }

        // If we passed Isha, the next prayer is Fajr tomorrow
        return NextPrayer("SUBUH", timings.fajr.take(5), true)
    }
}
