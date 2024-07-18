package me.thcr.toonysmp.custom_recipes

import me.thcr.toonysmp.config.ConfigWrapper
import org.bukkit.Server

@Suppress("UNUSED_PARAMETER")
class RecipeManager(server: Server, config_wrapper: ConfigWrapper) {
    init {
        CustomRecipe.entries.forEach {
            server.addRecipe(it.recipe)
        }
    }
}