package me.thcr.toonysmp.twitch

import com.google.gson.annotations.Expose

class AppInformation(@Expose val client_id: String, @Expose val client_secret: String, @Expose val redirect_uri: String)