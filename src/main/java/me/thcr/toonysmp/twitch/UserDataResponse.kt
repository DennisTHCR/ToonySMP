package me.thcr.toonysmp.twitch

import com.google.gson.annotations.Expose

data class UserDataResponse(@Expose val data: List<Data>)

data class Data(
    @Expose
    val id: String,
    @Expose
    val login: String,
    @Expose
    val display_name: String,
    @Expose
    val type: String,
    @Expose
    val broadcaster_type: String,
    @Expose
    val description: String,
    @Expose
    val profile_image_url: String,
    @Expose
    val offline_image_url: String,
    @Expose
    val view_count: Int,
    @Expose
    val created_at: String
)
