package me.thcr.toonysmp.config

import com.google.gson.annotations.Expose
import java.lang.reflect.Field
import java.util.logging.Logger

/**
 * Supported Types:
 * Boolean
 *
 */
class Config(private val logger: Logger) {
    @Expose
    var ENABLE_TWITCH_AUTOMATION: Boolean = false
    @Expose
    var ENABLE_COLORED_ITEM_NAMES: Boolean = false

    private var field_map: MutableMap<String, Field> = HashMap()

    init {
        this.javaClass.declaredFields.forEach { field ->
            field.isAccessible = true
            field_map[field.name] = field
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(field: String): T? {
        if (!field_map.containsKey(field)) return null
        return field_map[field]?.get(this) as T
    }

    fun <T> set(field: String, value: T) {
        if (!field_map.containsKey(field)) {
            return
        }
        field_map[field]?.set(this, value)
    }

    fun get_type(field: String): Class<*>? {
        if (!field_map.containsKey(field)) {
            logger.warning("Field $field does not exist!")
            return null
        }
        return field_map[field]?.type
    }
}

enum class ConfigOption {
    ENABLE_TWITCH_AUTOMATION,
    ENABLE_COLORED_ITEM_NAMES;
    init {
        @Suppress("UNUSED_VARIABLE") var field: String = this.name
    }
}