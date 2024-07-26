package me.thcr.toonysmp.custom_recipes

import me.thcr.toonysmp.Main
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

class DyeCrafts {
    class RedDyeRecipe : ShapelessRecipe(NamespacedKey(Main.instance.PLUGIN_NAME.lowercase(), "redstone_to_red_dye"), ItemStack(Material.RED_DYE, 1)) {
        init {
            addIngredient(ItemStack(Material.REDSTONE, 1))
        }
    }
    class BlackDyeRecipe : ShapelessRecipe(NamespacedKey(Main.instance.PLUGIN_NAME.lowercase(), "coal_to_black_dye"), ItemStack(Material.BLACK_DYE, 1)) {
        init {
            addIngredient(ItemStack(Material.COAL, 1))
        }
    }
}