package me.thcr.toonysmp.serde

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Deserializer {
    private val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    fun <T> deserialize_class(text: String, clazz: Class<T>): T {
        return gson.fromJson(text, clazz)
    }
}