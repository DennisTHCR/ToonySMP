package me.thcr.toonysmp.custom_recipes

import org.bukkit.inventory.Recipe

enum class CustomRecipe(val recipe: Recipe) {
    REDSTONE_TO_RED_DYE(DyeCrafts.RedDyeRecipe()),
    COAL_TO_BLACK_DYE(DyeCrafts.BlackDyeRecipe());
}

class CustomRecipeConfig {

}