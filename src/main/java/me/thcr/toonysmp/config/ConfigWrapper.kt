package me.thcr.toonysmp.config

import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.serde.Deserializer
import me.thcr.toonysmp.serde.Serde
import me.thcr.toonysmp.serde.Serializer
import java.util.logging.Logger

class ConfigWrapper(private val file_manager: FileManager, serde: Serde, private val config_path: String, logger: Logger) {
    private var config: Config = Config(logger)
    private val deserializer: Deserializer = serde.deserializer
    private val serializer: Serializer = serde.serializer
    private val subscription_map: MutableMap<String, MutableMap<String, Runnable>> = HashMap()

    fun read_from_file() {
        val text = file_manager.read_file(config_path)
        val deserialized = deserializer.deserialize_class(text, Config::class.java)
        config.config_map.putAll(deserialized.config_map)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun write_to_file() {
        val text = serializer.serialize_class(config)
        file_manager.write_file(config_path, text)
    }

    fun set(config_option: ConfigOption, value: Any) {
        config.set(config_option, value)
        call(config_option)
        write_to_file()
    }

    fun <T> get(config_option: ConfigOption): T {
        return config.get(config_option)!!
    }

    fun get_type(config_option: ConfigOption): Class<*> {
        return config.get_type(config_option)!!
    }

    @Suppress("unused")
    fun subscribe(config_option: ConfigOption, id: String, runnable: Runnable) {
        if (subscription_map[config_option.name] !is MutableMap) subscription_map[config_option.name] = HashMap()
        subscription_map[config_option.name]?.put(id, runnable)
    }

    @Suppress("unused")
    fun unsubscribe(config_option: ConfigOption, id: String) {
        subscription_map[config_option.name]?.remove(id)
    }

    private fun call(config_option: ConfigOption) {
        subscription_map[config_option.name]?.values?.forEach { it.run() }
    }
}
