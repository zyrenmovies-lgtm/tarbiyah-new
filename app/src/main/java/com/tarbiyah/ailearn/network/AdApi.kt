package com.tarbiyah.ailearn.network

import com.tarbiyah.ailearn.data.model.AdResponse
import retrofit2.http.GET

interface AdApi {
    @GET("api/ads")
    suspend fun getAds(): AdResponse
}
