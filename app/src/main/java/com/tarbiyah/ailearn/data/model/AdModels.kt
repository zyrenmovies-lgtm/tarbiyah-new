package com.tarbiyah.ailearn.data.model

import com.google.gson.annotations.SerializedName

data class AdResponse(
    @SerializedName("isActive") val isActive: Boolean = false,
    @SerializedName("title") val title: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("targetUrl") val targetUrl: String? = null
)
