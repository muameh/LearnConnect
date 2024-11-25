package com.mehmetbaloglu.learnconnect.data.models.CourseDetails


import com.google.gson.annotations.SerializedName

data class CourseDetails(
    @SerializedName("hits")
    val hits: List<Hit?>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("totalHits")
    val totalHits: Int?
)