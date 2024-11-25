package com.mehmetbaloglu.learnconnect.data.models.CourseDetails


import com.google.gson.annotations.SerializedName

data class Small(
    @SerializedName("height")
    val height: Int?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("width")
    val width: Int?
)