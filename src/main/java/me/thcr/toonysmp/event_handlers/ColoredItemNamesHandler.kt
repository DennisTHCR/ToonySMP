package me.thcr.toonysmp.event_handlers

import me.thcr.toonysmp.config.ConfigOption
import me.thcr.toonysmp.config.ConfigWrapper
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent

class ColoredItemNamesHandler(private val mm: MiniMessage, config_wrapper: ConfigWrapper) : Listener {
    private var enabled = false
    init {
        enabled = config_wrapper.get(ConfigOption.ENABLE_COLORED_ITEM_NAMES)
        config_wrapper.subscribe(ConfigOption.ENABLE_COLORED_ITEM_NAMES, "colored_item_name_handler") {
            enabled = config_wrapper.get(ConfigOption.ENABLE_COLORED_ITEM_NAMES)
        }
    }
    @EventHandler
    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        if (event.result == null || event.result?.isEmpty!! || !event.result?.itemMeta?.hasDisplayName()!! || !enabled) return
        val itemMeta = event.result?.itemMeta
        val name = itemMeta?.displayName() as TextComponent
        itemMeta.displayName(mm.deserialize(name.content()))
        event.result!!.itemMeta = itemMeta
    }
}