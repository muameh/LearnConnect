package com.mehmetbaloglu.learnconnect.data.models.CourseDetails


import com.google.gson.annotations.SerializedName

data class Videos(
    @SerializedName("large")
    val large: Large?,
    @SerializedName("medium")
    val medium: Medium?,
    @SerializedName("small")
    val small: Small?,
    @SerializedName("tiny")
    val tiny: Tiny?
)