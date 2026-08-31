package com.tarbiyah.ailearn.network

import com.tarbiyah.ailearn.data.model.SurahDetailResponse
import com.tarbiyah.ailearn.data.model.SurahListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApi {
    @GET("surat")
    suspend fun getSurahList(): SurahListResponse

    @GET("surat/{nomor}")
    suspend fun getSurahDetail(@Path("nomor") nomorSurah: Int): SurahDetailResponse
}
