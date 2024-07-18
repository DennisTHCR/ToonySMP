package me.thcr.toonysmp.event_handlers

import me.thcr.toonysmp.Main
import me.thcr.toonysmp.config.ConfigWrapper
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Server

class EventHandlerManager(server: Server, plugin: Main, mm: MiniMessage, config_wrapper: ConfigWrapper) {
    init {
        server.pluginManager.registerEvents(ColoredItemNamesHandler(mm, config_wrapper), plugin)
    }
}