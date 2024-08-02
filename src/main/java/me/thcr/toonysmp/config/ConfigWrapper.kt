package me.thcr.toonysmp.config

import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.serde.Deserializer
import me.thcr.toonysmp.serde.Serde
import me.thcr.toonysmp.serde.Serializer
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap

class ConfigWrapper<T>(
    private val file_manager: FileManager,
    serde: Serde,
    private val config_path: String,
    logger: Logger,
    private val option_class: Class<T>
) where T : Enum<T>,
        T : ConfigOptionInterface {

    private var config: Config<T> = Config(logger, option_class)
    private val deserializer: Deserializer = serde.deserializer
    private val serializer: Serializer = serde.serializer
    private val subscription_map: MutableMap<String, MutableMap<String, Runnable>> = HashMap()

    init {
        read_from_file()
    }

    fun read_from_file() {
        val text = file_manager.read_file(config_path)
        val deserialized = deserializer.deserialize_class(text, Map::class.java)
        val enumMap: MutableMap<T, Any> = EnumMap(option_class)
        deserialized.keys.forEach {
            val enumKey = java.lang.Enum.valueOf(option_class, it as String)
            enumMap[enumKey] = deserialized[it]!!
        }
        config.config_map.putAll(enumMap)
    }

    fun write_to_file() {
        val text = serializer.serialize_class(config)
        file_manager.write_file(config_path, text)
    }

    fun set(configOption: T, value: Any) {
        config.set(configOption, value)
        call(configOption)
        write_to_file()
    }

    fun <U> get(configOption: T): U {
        return config.get(configOption)!!
    }

    fun get_type(configOption: T): Class<*> {
        return config.get_type(configOption)!!
    }

    fun subscribe(configOption: T, id: String, runnable: Runnable) {
        if (subscription_map[configOption.name] !is MutableMap) subscription_map[configOption.name] = HashMap()
        subscription_map[configOption.name]?.put(id, runnable)
    }

    fun unsubscribe(configOption: T, id: String) {
        subscription_map[configOption.name]?.remove(id)
    }

    private fun call(configOption: T) {
        subscription_map[configOption.name]?.values?.forEach { it.run() }
    }
}
