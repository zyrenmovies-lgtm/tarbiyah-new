package com.tarbiyah.ailearn.data.model

import com.google.gson.annotations.SerializedName

data class SurahListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Surah>
)

data class SurahDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SurahDetail
)

data class Surah(
    @SerializedName("nomor") val nomor: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("namaLatin") val namaLatin: String,
    @SerializedName("jumlahAyat") val jumlahAyat: Int,
    @SerializedName("tempatTurun") val tempatTurun: String,
    @SerializedName("arti") val arti: String,
    @SerializedName("deskripsi") val deskripsi: String
)

data class SurahDetail(
    @SerializedName("nomor") val nomor: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("namaLatin") val namaLatin: String,
    @SerializedName("jumlahAyat") val jumlahAyat: Int,
    @SerializedName("tempatTurun") val tempatTurun: String,
    @SerializedName("arti") val arti: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("ayat") val ayat: List<Ayah>
)

data class Ayah(
    @SerializedName("nomorAyat") val nomorAyat: Int,
    @SerializedName("teksArab") val teksArab: String,
    @SerializedName("teksLatin") val teksLatin: String,
    @SerializedName("teksIndonesia") val teksIndonesia: String
)
