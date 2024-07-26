package me.thcr.toonysmp.custom_recipes

import com.google.gson.annotations.Expose
import org.bukkit.inventory.Recipe

enum class CustomRecipe(val recipe: Recipe) {
    REDSTONE_TO_RED_DYE(DyeCrafts.RedDyeRecipe()),
    COAL_TO_BLACK_DYE(DyeCrafts.BlackDyeRecipe());
}

@Suppress("unused")
class CustomRecipeConfig {
    @Expose
    val REDSTONE_TO_RED_DYE = true
    @Expose
    val COAL_TO_BLACK_DYE = true
}