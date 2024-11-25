package com.mehmetbaloglu.learnconnect.data.models.CourseDetails


import com.google.gson.annotations.SerializedName

data class Hit(
    @SerializedName("comments")
    val comments: Int?,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("pageURL")
    val pageURL: String?,
    @SerializedName("tags")
    val tags: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("user")
    val user: String?,
    @SerializedName("userImageURL")
    val userImageURL: String?,
    @SerializedName("user_id")
    val userİd: Int?,
    @SerializedName("videos")
    val videos: Videos?,
    @SerializedName("views")
    val views: Int?
)