package me.thcr.toonysmp.config

import java.util.*
import java.util.logging.Logger

class Config<T>(private val logger: Logger, optionClass: Class<T>)
    where T : Enum<T>,
          T : ConfigOptionInterface {

    val config_map: MutableMap<T, Any> = EnumMap(optionClass)

    init {
        load_defaults(optionClass)
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> get(option: T): U? {
        return config_map[option] as? U
    }

    fun <U> set(option: T, value: U) {
        if (config_map.containsKey(option)) {
            config_map[option] = value as Any
        } else {
            logger.warning("Config option ${option.name} does not exist!")
        }
    }

    fun get_type(option: T): Class<*>? {
        return config_map[option]?.javaClass
    }

    private fun load_defaults(optionClass: Class<T>) {
        optionClass.enumConstants?.forEach {
            config_map[it] = it.default_value
        }
    }
}

interface ConfigOptionInterface {
    val default_value: Any
    val configurable: Boolean
}

enum class GeneralConfig(override val default_value: Any, override val configurable: Boolean) : ConfigOptionInterface {
    ENABLE_TWITCH_AUTOMATION(true, false),
    ENABLE_COLORED_ITEM_NAMES(true, false);
}
