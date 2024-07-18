package me.thcr.toonysmp.serde

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Serialization wrapper.
 */
class Serializer {
    private val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    /**
     * Returns JSON representation of input class.
     */
    fun <T> serialize_class(value: T): String {
        return gson.toJson(value)
    }
}