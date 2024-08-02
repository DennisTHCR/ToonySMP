package me.thcr.toonysmp.custom_recipes

import org.bukkit.Server

class RecipeManager(server: Server) {
    init {
        CustomRecipe.entries.forEach {
            server.addRecipe(it.recipe)
        }
    }
}