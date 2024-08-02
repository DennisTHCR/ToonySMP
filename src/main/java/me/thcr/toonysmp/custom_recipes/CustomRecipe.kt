package me.thcr.toonysmp.custom_recipes

import me.thcr.toonysmp.config.ConfigOptionInterface
import org.bukkit.inventory.Recipe

enum class CustomRecipe(val recipe: Recipe) {
    REDSTONE_TO_RED_DYE(DyeCrafts.RedDyeRecipe()),
    COAL_TO_BLACK_DYE(DyeCrafts.BlackDyeRecipe());
}

enum class RecipeConfig(override val default_value: Any, override val configurable: Boolean) : ConfigOptionInterface {
    REDSTONE_TO_RED_DYE(true, false),
    COAL_TO_BLACK_DYE(true, false);
}