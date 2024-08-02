package me.thcr.toonysmp.config

import me.thcr.toonysmp.custom_recipes.RecipeConfig
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.serde.Serde
import java.util.logging.Logger

class ConfigManager(fileManager: FileManager, serde: Serde, basePath: String, logger: Logger) {
    val general_config: ConfigWrapper<GeneralConfig> = ConfigWrapper(fileManager, serde, "$basePath/config.json", logger, GeneralConfig::class.java)
    val recipe_config: ConfigWrapper<RecipeConfig> = ConfigWrapper(fileManager, serde, "$basePath/recipes.json", logger, RecipeConfig::class.java)
}