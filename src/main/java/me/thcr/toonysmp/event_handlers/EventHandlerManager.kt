package me.thcr.toonysmp.event_handlers

import me.thcr.toonysmp.Main
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.luckperms.GroupManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Server

class EventHandlerManager(server: Server, plugin: Main, mm: MiniMessage, config_wrapper: ConfigWrapper, group_manager: GroupManager) {
    init {
        val scoreboard = plugin.server.scoreboardManager.mainScoreboard
        server.pluginManager.registerEvents(ColoredItemNamesHandler(mm, config_wrapper), plugin)
        server.pluginManager.registerEvents(ChatPrefixHandler(mm, group_manager), plugin)
        server.pluginManager.registerEvents(TeamOnJoinHandler(scoreboard, group_manager), plugin)
    }
}