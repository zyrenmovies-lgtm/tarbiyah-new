package com.tarbiyah.ailearn.network

import com.tarbiyah.ailearn.data.model.PrayerTimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerApi {
    @GET("timingsByCity")
    suspend fun getPrayerTimes(
        @Query("city") city: String = "Jakarta",
        @Query("country") country: String = "Indonesia",
        @Query("method") method: Int = 20 // 20: Kemenag Indonesia
    ): PrayerTimeResponse
}
