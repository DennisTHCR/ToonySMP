package me.thcr.toonysmp.twitch

import com.google.gson.annotations.Expose

class DataObject(@Expose val topics: List<String>, @Expose val auth_token: String)