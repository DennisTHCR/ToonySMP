package me.thcr.toonysmp.config

import java.util.*
import java.util.logging.Logger

class Config(private val logger: Logger) {

    val config_map: MutableMap<ConfigOption, Any> = EnumMap(ConfigOption::class.java)

    init {
        load_defaults()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(option: ConfigOption): T? {
        return config_map[option] as? T
    }

    fun <T> set(option: ConfigOption, value: T) {
        if (config_map.containsKey(option)) {
            config_map[option] = value as Any
        } else {
            logger.warning("Config option ${option.name} does not exist!")
        }
    }

    fun get_type(option: ConfigOption): Class<*>? {
        return config_map[option]?.javaClass
    }

    private fun load_defaults() {
        config_map.keys.forEach {
            config_map[it] = it.default_value
        }
    }
}

enum class ConfigOption(val default_value: Any, val configurable: Boolean) {
    ENABLE_TWITCH_AUTOMATION(true, false),
    ENABLE_COLORED_ITEM_NAMES(true, false)
}
