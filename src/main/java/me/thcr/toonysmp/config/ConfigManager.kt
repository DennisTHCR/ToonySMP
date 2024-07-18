package me.thcr.toonysmp.config

import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.serde.Serde
import java.util.logging.Logger

class ConfigManager(file_manager: FileManager, serde: Serde, config_path: String, logger: Logger) {
    val config_wrapper: ConfigWrapper = ConfigWrapper(file_manager, serde, config_path, logger)
    init {
        config_wrapper.read_from_file()
    }
}