package me.thcr.toonysmp.event_handlers

import me.thcr.toonysmp.Main
import me.thcr.toonysmp.build_mode.QuickTest
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.config.GeneralConfig
import me.thcr.toonysmp.luckperms.GroupManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Server

class EventHandlerManager(server: Server, plugin: Main, mm: MiniMessage, configWrapper: ConfigWrapper<GeneralConfig>, groupManager: GroupManager) {
    init {
        val scoreboard = plugin.server.scoreboardManager.mainScoreboard
        server.pluginManager.registerEvents(ColoredItemNamesHandler(mm, configWrapper), plugin)
        server.pluginManager.registerEvents(ChatPrefixHandler(mm, groupManager), plugin)
        server.pluginManager.registerEvents(TeamOnJoinHandler(scoreboard, groupManager), plugin)
        server.pluginManager.registerEvents(QuickTest(), plugin)
    }
}