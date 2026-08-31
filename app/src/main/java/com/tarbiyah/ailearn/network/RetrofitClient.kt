package com.tarbiyah.ailearn.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://equran.id/api/v2/"

    val instance: QuranApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        retrofit.create(QuranApi::class.java)
    }

    private const val PRAYER_BASE_URL = "https://api.aladhan.com/v1/"

    val prayerInstance: PrayerApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(PRAYER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        retrofit.create(PrayerApi::class.java)
    }

    // Vercel Admin Web URL
    private const val AD_BASE_URL = "https://sitika-admin.vercel.app/"

    val adInstance: AdApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(AD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        retrofit.create(AdApi::class.java)
    }
}
